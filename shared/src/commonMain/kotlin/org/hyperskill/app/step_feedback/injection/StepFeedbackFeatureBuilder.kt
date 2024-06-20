package org.hyperskill.app.step_feedback.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.wrapWithAnalyticLogger
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_feedback.presentation.StepFeedbackActionDispatcher
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.Action
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.Message
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.ViewState
import org.hyperskill.app.step_feedback.presentation.StepFeedbackReducer
import org.hyperskill.app.step_feedback.view.StepFeedbackViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object StepFeedbackFeatureBuilder {
    private const val LOG_TAG = "StepFeedbackFeature"

    fun build(
        stepRoute: StepRoute,
        analyticInteractor: AnalyticInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<ViewState, Message, Action> {
        val stepFeedbackReducer =
            StepFeedbackReducer(stepRoute)
                .wrapWithLogger(buildVariant, logger, LOG_TAG)

        val stepFeedbackActionDispatcher = StepFeedbackActionDispatcher(
            ActionDispatcherOptions(),
            analyticInteractor
        )

        return ReduxFeature(
            initialState = StepFeedbackFeature.initialState(),
            reducer = stepFeedbackReducer
        )
            .wrapWithActionDispatcher(stepFeedbackActionDispatcher)
            .transformState(StepFeedbackViewStateMapper::map)
            .wrapWithAnalyticLogger(analyticInteractor) {
                (it as? StepFeedbackFeature.InternalAction.LogAnalyticEvent)?.analyticEvent
            }
    }
}