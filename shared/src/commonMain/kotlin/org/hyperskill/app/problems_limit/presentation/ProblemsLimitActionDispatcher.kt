package org.hyperskill.app.problems_limit.presentation

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.Timer
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.Action
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.Message
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ProblemsLimitActionDispatcher(
    config: ActionDispatcherOptions,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    init {
        currentSubscriptionStateRepository.changes
            .onEach {
                onNewMessage(Message.SubscriptionLoadingResult.Success(it))
            }
            .launchIn(actionScope)
    }

    private var timer: Timer? = null
    private val timerMutex = Mutex()

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.LoadSubscription -> {
                onNewMessage(
                    if (action.forceUpdate) {
                        currentSubscriptionStateRepository.reloadState()
                    } else {
                        currentSubscriptionStateRepository.getState()
                    }
                        .fold(
                            onSuccess = { Message.SubscriptionLoadingResult.Success(it) },
                            onFailure = { Message.SubscriptionLoadingResult.Error }
                        )
                )
            }
            is Action.LaunchTimer -> {
                timerMutex.withLock {
                    timer?.stop()

                    timer = Timer(
                        duration = action.updateIn,
                        onChange = { onNewMessage(Message.UpdateInChanged(it)) },
                        onFinish = { onNewMessage(Message.Initialize(forceUpdate = true)) },
                        launchIn = actionScope
                    )

                    timer?.run()
                }
            }
        }
    }
}