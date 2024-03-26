package org.hyperskill.app.theory_feedback.presentation

import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.theory_feedback.domain.analytic.TheoryFeedbackModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.theory_feedback.domain.analytic.TheoryFeedbackModalSendButtonClickedHyperskillAnalyticEvent
import org.hyperskill.app.theory_feedback.domain.analytic.TheoryFeedbackModalShownHyperskillAnalyticEvent
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.Action
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.InternalAction
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.Message
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias TheoryFeedbackReducerResult = Pair<State, Set<Action>>

internal class TheoryFeedbackReducer(
    private val stepRoute: StepRoute
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): TheoryFeedbackReducerResult =
        when (message) {
            Message.AlertShown -> handleAlertShown(state)
            Message.AlertHidden -> handleAlertHidden(state)
            Message.SendButtonClicked -> handleSendButtonClicked(state)
            is Message.FeedbackTextChanged -> handleFeedbackTextChanged(state, message)
        }

    private fun handleAlertShown(state: State): TheoryFeedbackReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                TheoryFeedbackModalShownHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    stepId = stepRoute.stepId
                )
            )
        )

    private fun handleAlertHidden(state: State): TheoryFeedbackReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                TheoryFeedbackModalHiddenHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    stepId = stepRoute.stepId
                )
            )
        )

    private fun handleFeedbackTextChanged(
        state: State,
        message: Message.FeedbackTextChanged
    ): TheoryFeedbackReducerResult =
        state.copy(feedback = message.text) to emptySet()

    private fun handleSendButtonClicked(state: State): TheoryFeedbackReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                TheoryFeedbackModalSendButtonClickedHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    stepId = stepRoute.stepId,
                    feedback = state.feedback ?: ""
                )
            ),
            Action.ViewAction.ShowSendSuccessAndHideModal
        )
}