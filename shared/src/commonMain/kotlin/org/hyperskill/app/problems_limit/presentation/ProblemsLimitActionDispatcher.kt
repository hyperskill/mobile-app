package org.hyperskill.app.problems_limit.presentation

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.Timer
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.Action
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.InternalAction
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.InternalMessage
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.Message
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile.domain.repository.isFreemiumWrongSubmissionChargeLimitsEnabled
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ProblemsLimitActionDispatcher(
    config: ActionDispatcherOptions,
    private val freemiumInteractor: FreemiumInteractor,
    private val sentryInteractor: SentryInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    init {
        currentSubscriptionStateRepository.changes
            .onEach {
                onNewMessage(InternalMessage.SubscriptionChanged(it))
            }
            .launchIn(actionScope)
    }

    private var timer: Timer? = null
    private val timerMutex = Mutex()

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.LoadSubscription ->
                handleLoadSubscriptionAction(action, ::onNewMessage)
            is InternalAction.LaunchTimer -> {
                timerMutex.withLock {
                    timer?.stop()

                    timer = Timer(
                        duration = action.updateIn,
                        onChange = { onNewMessage(InternalMessage.UpdateInChanged(it)) },
                        onFinish = {
                            currentSubscriptionStateRepository.resetState()
                            onNewMessage(InternalMessage.Initialize(forceUpdate = true))
                        },
                        launchIn = actionScope
                    )

                    timer?.start()
                }
            }
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
        }
    }

    private suspend fun handleLoadSubscriptionAction(
        action: InternalAction.LoadSubscription,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            action.screen.sentryTransaction,
            onError = { InternalMessage.LoadSubscriptionResultError }
        ) {
            val isFreemiumEnabled = freemiumInteractor.isFreemiumEnabled().getOrThrow()
            val isFreemiumWrongSubmissionChargeLimitsEnabled =
                currentProfileStateRepository.isFreemiumWrongSubmissionChargeLimitsEnabled()

            val currentSubscription = currentSubscriptionStateRepository
                .getState(forceUpdate = action.forceUpdate)
                .getOrThrow()

            InternalMessage.LoadSubscriptionResultSuccess(
                subscription = currentSubscription,
                isFreemiumEnabled = isFreemiumEnabled,
                isFreemiumWrongSubmissionChargeLimitsEnabled = isFreemiumWrongSubmissionChargeLimitsEnabled
            )
        }.let(onNewMessage)
    }
}