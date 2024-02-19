package org.hyperskill.app.request_review.injection

import org.hyperskill.app.core.flowredux.presentation.wrapWithFlowView
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.request_review.modal.injection.RequestReviewModalComponent
import org.hyperskill.app.request_review.presentation.RequestReviewModalViewModel

class PlatformRequestReviewComponentImpl(
    private val requestReviewComponent: RequestReviewModalComponent
) : PlatformRequestReviewComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                RequestReviewModalViewModel::class.java to {
                    RequestReviewModalViewModel(
                        requestReviewComponent.requestReviewModalFeature.wrapWithFlowView()
                    )
                }
            )
        )
}