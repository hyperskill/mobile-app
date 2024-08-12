package org.hyperskill.app.request_review.modal.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.Action
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.Message
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.ViewState
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.presentation.redux.feature.Feature

internal class RequestReviewModalComponentImpl(
    private val appGraph: AppGraph,
    private val stepRoute: StepRoute
) : RequestReviewModalComponent {
    override val requestReviewModalFeature: Feature<ViewState, Message, Action>
        get() = RequestReviewModalFeatureBuilder.build(
            stepRoute = stepRoute,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant,
            resourceProvider = appGraph.commonComponent.resourceProvider
        )
}