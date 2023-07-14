package org.hyperskill.app.progress.presentation

import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.progress_screen.presentation.ProgressScreenFeature.Action
import org.hyperskill.app.progress_screen.presentation.ProgressScreenFeature.Message
import org.hyperskill.app.progress_screen.view.ProgressScreenViewState

class ProgressScreenViewModel(
    reduxViewContainer: FlowView<ProgressScreenViewState, Message, Action>
) : ReduxFlowViewModel<ProgressScreenViewState, Message, Action>(reduxViewContainer) {

    init {
        onNewMessage(Message.Initialize)
    }
}