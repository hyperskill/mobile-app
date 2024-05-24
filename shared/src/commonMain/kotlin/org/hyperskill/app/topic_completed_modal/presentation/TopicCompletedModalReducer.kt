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
            Message.CloseButtonClicked -> handleCloseButtonClicked(state)
            Message.CallToActionButtonClicked -> handleCallToActionButtonClicked(state)
            Message.ShownEventMessage -> handleShownEventMessage(state)
            Message.HiddenEventMessage -> handleHiddenEventMessage(state)
        }

    private fun handleCloseButtonClicked(state: State): TopicCompletedModalReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(TopicCompletedModalClickedCloseHyperskillAnalyticEvent(analyticRoute)),
            Action.ViewAction.Dismiss
        )

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

    private fun handleShownEventMessage(state: State): TopicCompletedModalReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(TopicCompletedModalShownHyperskillAnalyticEvent(analyticRoute))
        )

    private fun handleHiddenEventMessage(state: State): TopicCompletedModalReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(TopicCompletedModalHiddenHyperskillAnalyticEvent(analyticRoute))
        )
}