package org.hyperskill.app.stage_implement.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.stage_implement.domain.analytic.ProjectCompletedModalClickedGoToStudyPlanHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.ProjectCompletedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.ProjectCompletedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageCompletedModalClickedGoToStudyPlanHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageCompletedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageCompletedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementViewedHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Action
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.InternalAction
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.InternalMessage
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Message
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal class StageImplementReducer(
    private val analyticRoute: HyperskillAnalyticRoute
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize -> {
                if (state is State.Idle ||
                    (message.forceUpdate && state is State.NetworkError)
                ) {
                    State.Loading to setOf(
                        InternalAction.FetchStageImplement(
                            projectId = message.projectId,
                            stageId = message.stageId
                        )
                    )
                } else {
                    null
                }
            }
            is InternalMessage.FetchStageImplementFailure ->
                State.NetworkError to emptySet()
            is InternalMessage.FetchStageImplementSuccess ->
                State.Content(message.projectId, message.stage) to emptySet()
            is InternalMessage.StepSolved ->
                if (state is State.Content && message.stepId == state.stage.stepId) {
                    state to setOf(InternalAction.CheckStageCompletionStatus(state.stage))
                } else {
                    null
                }
            is InternalMessage.StageCompleted ->
                state to setOf(
                    Action.ViewAction.ShowStageCompletedModal(
                        title = message.title,
                        stageAward = message.stageAward
                    )
                )
            Message.StageCompletedModalGoToStudyPlanClicked ->
                state to setOf(
                    Action.ViewAction.NavigateTo.StudyPlan,
                    InternalAction.LogAnalyticEvent(
                        StageCompletedModalClickedGoToStudyPlanHyperskillAnalyticEvent(analyticRoute)
                    )
                )
            is InternalMessage.ProjectCompleted ->
                state to setOf(
                    Action.ViewAction.ShowProjectCompletedModal(
                        stageAward = message.stageAward,
                        projectAward = message.projectAward
                    )
                )
            Message.ProjectCompletedModalGoToStudyPlanClicked ->
                state to setOf(
                    Action.ViewAction.NavigateTo.StudyPlan,
                    InternalAction.LogAnalyticEvent(
                        ProjectCompletedModalClickedGoToStudyPlanHyperskillAnalyticEvent(analyticRoute)
                    )
                )
            is Message.ViewedEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(StageImplementViewedHyperskillAnalyticEvent(analyticRoute))
                )
            Message.StageCompletedModalShownEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(StageCompletedModalShownHyperskillAnalyticEvent(analyticRoute))
                )
            Message.StageCompletedModalHiddenEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(StageCompletedModalHiddenHyperskillAnalyticEvent(analyticRoute))
                )
            Message.ProjectCompletedModalShownEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(ProjectCompletedModalShownHyperskillAnalyticEvent(analyticRoute))
                )
            Message.ProjectCompletedModalHiddenEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(ProjectCompletedModalHiddenHyperskillAnalyticEvent(analyticRoute))
                )
        } ?: (state to emptySet())
}