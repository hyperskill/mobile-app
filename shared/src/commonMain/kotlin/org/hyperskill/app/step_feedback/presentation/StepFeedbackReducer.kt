package org.hyperskill.app.step_feedback.presentation

import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_feedback.domain.analytic.StepFeedbackModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_feedback.domain.analytic.StepFeedbackModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.Action
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.InternalAction
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.InternalMessage
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.Message
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

internal class StepFeedbackReducer(
    private val stepRoute: StepRoute
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            Message.AlertShown -> handleAlertShown(state)
            Message.AlertHidden -> handleAlertHidden(state)
            Message.SendButtonClicked -> handleSendButtonClicked(state)
            is Message.FeedbackTextChanged -> handleFeedbackTextChanged(state, message)
            InternalMessage.FeedbackSent -> handleFeedbackSent(state)
        }

    private fun handleAlertShown(state: State): ReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                StepFeedbackModalShownHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    stepId = stepRoute.stepId
                )
            )
        )

    private fun handleAlertHidden(state: State): ReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                StepFeedbackModalHiddenHyperskillAnalyticEvent(
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
            InternalAction.SendFeedback(
                route = stepRoute.analyticRoute,
                stepId = stepRoute.stepId,
                feedback = state.feedback ?: ""
            )
        )

    private fun handleFeedbackSent(state: State): ReducerResult =
        state to setOf(Action.ViewAction.ShowSendSuccessAndHideModal)
}