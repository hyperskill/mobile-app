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
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ProblemsLimitActionDispatcher(
    config: ActionDispatcherOptions,
    private val freemiumInteractor: FreemiumInteractor,
    private val sentryInteractor: SentryInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository
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
            is InternalAction.LoadSubscription -> {
                val sentryTransaction = action.screen.sentryTransaction
                sentryInteractor.startTransaction(sentryTransaction)

                val isFreemiumEnabled = freemiumInteractor
                    .isFreemiumEnabled()
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        return onNewMessage(InternalMessage.LoadSubscriptionResultError)
                    }

                onNewMessage(
                    currentSubscriptionStateRepository.getState(forceUpdate = action.forceUpdate)
                        .fold(
                            onSuccess = {
                                sentryInteractor.finishTransaction(sentryTransaction)
                                InternalMessage.LoadSubscriptionResultSuccess(
                                    subscription = it,
                                    isFreemiumEnabled = isFreemiumEnabled
                                )
                            },
                            onFailure = {
                                sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                                InternalMessage.LoadSubscriptionResultError
                            }
                        )
                )
            }
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
}