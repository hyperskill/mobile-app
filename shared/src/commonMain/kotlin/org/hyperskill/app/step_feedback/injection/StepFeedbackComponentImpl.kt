package org.hyperskill.app.step_feedback.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.Action
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.Message
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class StepFeedbackComponentImpl(
    private val appGraph: AppGraph,
    private val stepRoute: StepRoute
) : StepFeedbackComponent {
    override val stepFeedbackFeature: Feature<ViewState, Message, Action>
        get() = StepFeedbackFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant,
            stepRoute = stepRoute
        )
}