package org.hyperskill.app.request_review.presentation

import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.Action.ViewAction
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.Message
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.ViewState

class RequestReviewModalViewModel(
    viewContainer: FlowView<ViewState, Message, ViewAction>
) : ReduxFlowViewModel<ViewState, Message, ViewAction>(viewContainer) {
    fun onPositiveButtonClick() {
        onNewMessage(Message.PositiveButtonClicked)
    }

    fun onNegativeButtonClick() {
        onNewMessage(Message.NegativeButtonClicked)
    }

    fun onShownEvent() {
        onNewMessage(Message.ShownEventMessage)
    }

    fun onHiddenEvent() {
        onNewMessage(Message.HiddenEventMessage)
    }
}