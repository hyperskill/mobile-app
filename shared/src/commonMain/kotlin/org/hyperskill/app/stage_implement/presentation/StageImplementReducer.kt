package org.hyperskill.app.stage_implement.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementClickedDeprecatedButtonHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementUnsupportedModalClickedGoToHomeScreenHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementUnsupportedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementUnsupportedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementViewedHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Action
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Message
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal class StageImplementReducer(
    private var analyticRoute: HyperskillAnalyticRoute? = null
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize -> {
                if (state is State.Idle ||
                    (message.forceUpdate && state is State.NetworkError)
                ) {
                    analyticRoute = HyperskillAnalyticRoute.Projects.Stages.Implement(
                        projectId = message.projectId,
                        stageId = message.stageId
                    )

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
                    state to buildSet {
                        add(Action.ViewAction.NavigateTo.Back)
                        analyticRoute?.let {
                            val analyticEvent = StageImplementClickedDeprecatedButtonHyperskillAnalyticEvent(it)
                            add(Action.LogAnalyticEvent(analyticEvent))
                        }
                    }
                } else {
                    null
                }
            is Message.ViewedEventMessage ->
                analyticRoute?.let {
                    state to setOf(Action.LogAnalyticEvent(StageImplementViewedHyperskillAnalyticEvent(it)))
                }
            // Unsupported modal
            is Message.UnsupportedModalShownEventMessage ->
                if (state is State.Unsupported) {
                    analyticRoute?.let {
                        val analyticEvent = StageImplementUnsupportedModalShownHyperskillAnalyticEvent(it)
                        state to setOf(Action.LogAnalyticEvent(analyticEvent))
                    }
                } else {
                    null
                }
            is Message.UnsupportedModalHiddenEventMessage ->
                if (state is State.Unsupported) {
                    analyticRoute?.let {
                        val analyticEvent = StageImplementUnsupportedModalHiddenHyperskillAnalyticEvent(it)
                        state to setOf(Action.LogAnalyticEvent(analyticEvent))
                    }
                } else {
                    null
                }
            is Message.UnsupportedModalGoToHomeScreenClicked ->
                if (state is State.Unsupported) {
                    state to buildSet {
                        add(Action.ViewAction.NavigateTo.HomeScreen)
                        analyticRoute?.let {
                            val analyticEvent =
                                StageImplementUnsupportedModalClickedGoToHomeScreenHyperskillAnalyticEvent(it)
                            add(Action.LogAnalyticEvent(analyticEvent))
                        }
                    }
                } else {
                    null
                }
        } ?: (state to emptySet())
}