package org.hyperskill.app.problems_limit.presentation

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.Timer
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.Action
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.Message
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ProblemsLimitActionDispatcher(
    config: ActionDispatcherOptions,
    private val sentryInteractor: SentryInteractor,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    init {
        currentSubscriptionStateRepository.changes
            .onEach {
                onNewMessage(Message.SubscriptionChanged(it))
            }
            .launchIn(actionScope)
    }

    private var timer: Timer? = null
    private val timerMutex = Mutex()

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.LoadSubscription -> {
                sentryInteractor.withTransaction(
                    transaction = action.screen.sentryTransaction,
                    onError = { Message.SubscriptionLoadingResult.Error }
                ) {
                    Message.SubscriptionLoadingResult.Success(
                        subscription = currentSubscriptionStateRepository
                            .getState(forceUpdate = action.forceUpdate)
                            .getOrThrow()
                    )
                }.let(::onNewMessage)
            }
            is Action.LaunchTimer -> {
                timerMutex.withLock {
                    timer?.stop()

                    timer = Timer(
                        duration = action.updateIn,
                        onChange = { onNewMessage(Message.UpdateInChanged(it)) },
                        onFinish = {
                            currentSubscriptionStateRepository.resetState()
                            onNewMessage(Message.Initialize(forceUpdate = true))
                        },
                        launchIn = actionScope
                    )

                    timer?.start()
                }
            }
        }
    }
}