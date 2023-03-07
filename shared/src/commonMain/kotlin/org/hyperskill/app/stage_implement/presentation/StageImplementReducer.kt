package org.hyperskill.app.stage_implement.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementClickedProjectDeprecatedButtonHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementUnsupportedModalClickedGoToHomeScreenHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementUnsupportedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementUnsupportedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementViewedHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Action
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
                        Action.FetchStageImplement(projectId = message.projectId, stageId = message.stageId)
                    )
                } else {
                    null
                }
            }
            is Message.FetchStageImplementResult.Deprecated ->
                State.Deprecated(message.project) to emptySet()
            is Message.FetchStageImplementResult.Unsupported ->
                State.Unsupported to setOf(Action.ViewAction.ShowUnsupportedModal)
            is Message.FetchStageImplementResult.NetworkError ->
                State.NetworkError to emptySet()
            is Message.FetchStageImplementResult.Success ->
                State.Content(message.project, message.stage, message.step) to emptySet()
            is Message.ProjectDeprecatedButtonClicked ->
                if (state is State.Deprecated) {
                    state to setOf(
                        Action.ViewAction.NavigateTo.Back,
                        Action.LogAnalyticEvent(
                            StageImplementClickedProjectDeprecatedButtonHyperskillAnalyticEvent(analyticRoute)
                        )
                    )
                } else {
                    null
                }
            is Message.ViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(StageImplementViewedHyperskillAnalyticEvent(analyticRoute)))
            // Unsupported modal
            is Message.UnsupportedModalShownEventMessage ->
                if (state is State.Unsupported) {
                    state to setOf(
                        Action.LogAnalyticEvent(
                            StageImplementUnsupportedModalShownHyperskillAnalyticEvent(analyticRoute)
                        )
                    )
                } else {
                    null
                }
            is Message.UnsupportedModalHiddenEventMessage ->
                if (state is State.Unsupported) {
                    state to setOf(
                        Action.LogAnalyticEvent(
                            StageImplementUnsupportedModalHiddenHyperskillAnalyticEvent(analyticRoute)
                        )
                    )
                } else {
                    null
                }
            is Message.UnsupportedModalGoToHomeScreenClicked ->
                if (state is State.Unsupported) {
                    state to setOf(
                        Action.ViewAction.NavigateTo.HomeScreen,
                        Action.LogAnalyticEvent(
                            StageImplementUnsupportedModalClickedGoToHomeScreenHyperskillAnalyticEvent(analyticRoute)
                        )
                    )
                } else {
                    null
                }
        } ?: (state to emptySet())
}