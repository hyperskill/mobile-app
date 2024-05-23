package org.hyperskill.app.topic_completed_modal.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.topic_completed_modal.domain.analytic.TopicCompletedModalClickedCloseHyperskillAnalyticEvent
import org.hyperskill.app.topic_completed_modal.domain.analytic.TopicCompletedModalClickedContinueNextTopicHyperskillAnalyticEvent
import org.hyperskill.app.topic_completed_modal.domain.analytic.TopicCompletedModalClickedGoToStudyPlanHyperskillAnalyticEvent
import org.hyperskill.app.topic_completed_modal.domain.analytic.TopicCompletedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.topic_completed_modal.domain.analytic.TopicCompletedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.Action
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.InternalAction
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.Message
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias TopicCompletedModalReducerResult = Pair<State, Set<Action>>

internal class TopicCompletedModalReducer(
    private val analyticRoute: HyperskillAnalyticRoute
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): TopicCompletedModalReducerResult =
        when (message) {
            Message.CloseButtonClicked -> {
                val event = TopicCompletedModalClickedCloseHyperskillAnalyticEvent(analyticRoute)
                state to setOf(InternalAction.LogAnalyticEvent(event), Action.ViewAction.Dismiss)
            }
            Message.CallToActionButtonClicked ->
                handleCallToActionButtonClicked(state)
            Message.ShownEventMessage -> {
                val event = TopicCompletedModalShownHyperskillAnalyticEvent(analyticRoute)
                state to setOf(InternalAction.LogAnalyticEvent(event))
            }
            Message.HiddenEventMessage -> {
                val event = TopicCompletedModalHiddenHyperskillAnalyticEvent(analyticRoute)
                state to setOf(InternalAction.LogAnalyticEvent(event))
            }
        }

    private fun handleCallToActionButtonClicked(state: State): TopicCompletedModalReducerResult =
        if (state.canContinueWithNextTopic) {
            state to setOf(
                InternalAction.LogAnalyticEvent(
                    TopicCompletedModalClickedContinueNextTopicHyperskillAnalyticEvent(analyticRoute)
                ),
                Action.ViewAction.NavigateTo.NextTopic
            )
        } else {
            state to setOf(
                InternalAction.LogAnalyticEvent(
                    TopicCompletedModalClickedGoToStudyPlanHyperskillAnalyticEvent(analyticRoute)
                ),
                Action.ViewAction.NavigateTo.StudyPlan
            )
        }
}