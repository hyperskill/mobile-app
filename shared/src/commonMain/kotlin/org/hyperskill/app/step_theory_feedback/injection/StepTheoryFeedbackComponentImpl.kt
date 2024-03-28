package org.hyperskill.app.step_theory_feedback.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.Action
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.Message
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class StepTheoryFeedbackComponentImpl(
    private val appGraph: AppGraph,
    private val stepRoute: StepRoute
) : StepTheoryFeedbackComponent {
    override val stepTheoryFeedbackFeature: Feature<ViewState, Message, Action>
        get() = StepTheoryFeedbackFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant,
            stepRoute = stepRoute
        )
}