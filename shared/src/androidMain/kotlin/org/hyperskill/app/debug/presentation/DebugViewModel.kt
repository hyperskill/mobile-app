package org.hyperskill.app.debug.presentation

import org.hyperskill.app.debug.presentation.DebugFeature.Action.ViewAction
import org.hyperskill.app.debug.presentation.DebugFeature.Message
import org.hyperskill.app.debug.presentation.DebugFeature.State
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class DebugViewModel(
    reduxViewContainer: ReduxViewContainer<State, Message, ViewAction>
) : ReduxViewModel<State, Message, ViewAction>(reduxViewContainer) {
    init {
        onNewMessage(Message.Initialize(forceUpdate = false))
    }
}