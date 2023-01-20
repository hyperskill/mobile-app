package org.hyperskill.app.debug.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.debug.domain.interactor.DebugInteractor
import org.hyperskill.app.debug.presentation.DebugFeature.Action
import org.hyperskill.app.debug.presentation.DebugFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class DebugActionDispatcher(
    config: ActionDispatcherOptions,
    private val debugInteractor: DebugInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchDebugSettings -> {
                val debugSettings = debugInteractor.fetchDebugSettings()
                onNewMessage(Message.FetchDebugSettingsSuccess(debugSettings))
            }
            is Action.UpdateEndpointConfig -> {
                // TODO: Clear cache, sign out and what else...
                debugInteractor.updateEndpointConfig(action.endpointConfigType)
                onNewMessage(Message.ApplySettingsSuccess)
            }
            else -> {}
        }
    }
}