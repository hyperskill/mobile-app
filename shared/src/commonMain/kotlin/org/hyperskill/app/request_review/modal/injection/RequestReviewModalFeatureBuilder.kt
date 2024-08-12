package org.hyperskill.app.request_review.modal.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.wrapWithAnalyticLogger
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.Action
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.Message
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.State
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.ViewState
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalReducer
import org.hyperskill.app.request_review.modal.view.mapper.RequestReviewModalViewStateMapper
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object RequestReviewModalFeatureBuilder {
    private const val LOG_TAG = "RequestReviewModalFeature"

    fun build(
        stepRoute: StepRoute,
        analyticInteractor: AnalyticInteractor,
        logger: Logger,
        buildVariant: BuildVariant,
        resourceProvider: ResourceProvider
    ): Feature<ViewState, Message, Action> {
        val requestReviewModalReducer = RequestReviewModalReducer(
            stepRoute = stepRoute,
            resourceProvider = resourceProvider
        ).wrapWithLogger(buildVariant, logger, LOG_TAG)

        val requestReviewModalViewStateMapper = RequestReviewModalViewStateMapper(
            resourceProvider = resourceProvider
        )

        return ReduxFeature(State.Awaiting, requestReviewModalReducer)
            .wrapWithAnalyticLogger(analyticInteractor) {
                (it as? RequestReviewModalFeature.InternalAction.LogAnalyticEvent)?.analyticEvent
            }
            .transformState(requestReviewModalViewStateMapper::map)
    }
}