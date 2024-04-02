package org.hyperskill.app.step_theory_feedback.presentation

import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_theory_feedback.domain.analytic.StepTheoryFeedbackModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_theory_feedback.domain.analytic.StepTheoryFeedbackModalSendButtonClickedHyperskillAnalyticEvent
import org.hyperskill.app.step_theory_feedback.domain.analytic.StepTheoryFeedbackModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.Action
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.InternalAction
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.Message
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

internal class StepTheoryFeedbackReducer(
    private val stepRoute: StepRoute
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            Message.AlertShown -> handleAlertShown(state)
            Message.AlertHidden -> handleAlertHidden(state)
            Message.SendButtonClicked -> handleSendButtonClicked(state)
            is Message.FeedbackTextChanged -> handleFeedbackTextChanged(state, message)
        }

    private fun handleAlertShown(state: State): ReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                StepTheoryFeedbackModalShownHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    stepId = stepRoute.stepId
                )
            )
        )

    private fun handleAlertHidden(state: State): ReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                StepTheoryFeedbackModalHiddenHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    stepId = stepRoute.stepId
                )
            )
        )

    private fun handleFeedbackTextChanged(
        state: State,
        message: Message.FeedbackTextChanged
    ): ReducerResult =
        state.copy(feedback = message.text) to emptySet()

    private fun handleSendButtonClicked(state: State): ReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                StepTheoryFeedbackModalSendButtonClickedHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    stepId = stepRoute.stepId,
                    feedback = state.feedback ?: ""
                )
            ),
            Action.ViewAction.ShowSendSuccessAndHideModal
        )
}