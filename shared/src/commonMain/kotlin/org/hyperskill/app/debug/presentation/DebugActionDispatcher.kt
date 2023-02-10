package org.hyperskill.app.debug.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.debug.domain.interactor.DebugInteractor
import org.hyperskill.app.debug.presentation.DebugFeature.Action
import org.hyperskill.app.debug.presentation.DebugFeature.Message
import org.hyperskill.app.main.domain.interactor.AppInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class DebugActionDispatcher(
    config: ActionDispatcherOptions,
    private val debugInteractor: DebugInteractor,
    private val appInteractor: AppInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchDebugSettings -> {
                val debugSettings = debugInteractor.fetchDebugSettings()
                onNewMessage(Message.FetchDebugSettingsSuccess(debugSettings))
            }
            is Action.UpdateEndpointConfig -> {
                appInteractor.doCurrentUserSignedOutCleanUp()

                debugInteractor.updateEndpointConfig(action.endpointConfig)
                onNewMessage(Message.ApplySettingsSuccess)
            }
            else -> {
                // no op
            }
        }
    }
}