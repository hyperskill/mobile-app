package org.hyperskill.app.request_review.modal.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.domain.platform.Platform
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.State
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.ViewState

internal class RequestReviewModalViewStateMapper(
    private val platform: Platform,
    private val resourceProvider: ResourceProvider
) {
    fun map(state: State): ViewState =
        when (state) {
            State.Awaiting, State.Positive ->
                ViewState(
                    title = resourceProvider.getString(
                        SharedResources.strings.request_review_modal_state_awaiting_title,
                        resourceProvider.getString(platform.appNameResource)
                    ),
                    description = null,
                    positiveButtonText = resourceProvider.getString(
                        SharedResources.strings.request_review_modal_state_awaiting_positive_button_text
                    ),
                    negativeButtonText = resourceProvider.getString(
                        SharedResources.strings.request_review_modal_state_awaiting_negative_button_text
                    ),
                    state = when (state) {
                        State.Awaiting -> ViewState.State.AWAITING
                        State.Positive -> ViewState.State.POSITIVE
                        State.Negative -> throw IllegalStateException("State.Negative shouldn't be mapped to ViewState")
                    }
                )
            State.Negative ->
                ViewState(
                    title = resourceProvider.getString(
                        SharedResources.strings.request_review_modal_state_negative_title
                    ),
                    description = resourceProvider.getString(
                        SharedResources.strings.request_review_modal_state_negative_description
                    ),
                    positiveButtonText = resourceProvider.getString(
                        SharedResources.strings.request_review_modal_state_negative_positive_button_text
                    ),
                    negativeButtonText = resourceProvider.getString(
                        SharedResources.strings.request_review_modal_state_negative_negative_button_text
                    ),
                    state = ViewState.State.NEGATIVE
                )
        }
}