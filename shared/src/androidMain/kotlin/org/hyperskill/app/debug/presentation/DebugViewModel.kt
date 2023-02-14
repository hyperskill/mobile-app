package org.hyperskill.app.debug.presentation

import org.hyperskill.app.debug.presentation.DebugFeature.Action.ViewAction
import org.hyperskill.app.debug.presentation.DebugFeature.Message
import org.hyperskill.app.debug.presentation.DebugFeature.ViewState
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class DebugViewModel(
    reduxViewContainer: ReduxViewContainer<ViewState, Message, ViewAction>
) : ReduxViewModel<ViewState, Message, ViewAction>(reduxViewContainer) {
    init {
        onNewMessage(Message.Initialize(forceUpdate = false))
    }
}