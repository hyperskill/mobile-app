package org.hyperskill.app.step_theory_feedback.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackActionDispatcher
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.Action
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.Message
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.ViewState
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackReducer
import org.hyperskill.app.step_theory_feedback.view.StepTheoryFeedbackViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object StepTheoryFeedbackFeatureBuilder {
    private const val LOG_TAG = "StepTheoryFeedbackFeature"

    fun build(
        stepRoute: StepRoute,
        analyticInteractor: AnalyticInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<ViewState, Message, Action> {
        val stepTheoryFeedbackReducer =
            StepTheoryFeedbackReducer(stepRoute)
                .wrapWithLogger(buildVariant, logger, LOG_TAG)

        val stepTheoryFeedbackActionDispatcher = StepTheoryFeedbackActionDispatcher(
            ActionDispatcherOptions(),
            analyticInteractor
        )

        return ReduxFeature(
            initialState = StepTheoryFeedbackFeature.initialState(),
            reducer = stepTheoryFeedbackReducer
        )
            .wrapWithActionDispatcher(stepTheoryFeedbackActionDispatcher)
            .transformState(StepTheoryFeedbackViewStateMapper::map)
    }
}