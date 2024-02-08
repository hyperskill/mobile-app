package org.hyperskill.app.request_review.modal.injection

import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.Action
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.Message
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface RequestReviewModalComponent {
    val requestReviewModalFeature: Feature<ViewState, Message, Action>
}