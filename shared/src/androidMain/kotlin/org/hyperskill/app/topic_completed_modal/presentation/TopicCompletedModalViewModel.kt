package org.hyperskill.app.topic_completed_modal.presentation

import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.Action.ViewAction
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.Message
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.ViewState

class TopicCompletedModalViewModel(
    viewContainer: FlowView<ViewState, Message, ViewAction>
) : ReduxFlowViewModel<ViewState, Message, ViewAction>(viewContainer) {

    fun onCTAClick() {
        onNewMessage(Message.CallToActionButtonClicked)
    }

    fun onCloseClick() {
        onNewMessage(Message.CloseButtonClicked)
    }
}