package org.hyperskill.app.android.request_review.ui

import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature

object RequestReviewPreviewDefaults {
    val AwaitingViewState: RequestReviewModalFeature.ViewState
        get() = RequestReviewModalFeature.ViewState(
            title = "Do you enjoy Hyperskill app?",
            description = null,
            positiveButtonText = "\uD83D\uDC4D Yes",
            negativeButtonText = "\uD83D\uDC4E No",
            state = RequestReviewModalFeature.ViewState.State.AWAITING
        )

    val NegativeViewState: RequestReviewModalFeature.ViewState
        get() = RequestReviewModalFeature.ViewState(
            title = "Thank you",
            description = "Share what you disliked to help us improve your experience.",
            positiveButtonText = "Write a request",
            negativeButtonText = "Maybe later",
            state = RequestReviewModalFeature.ViewState.State.NEGATIVE
        )
}