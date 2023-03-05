package org.hyperskill.app.stage_implement.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementClickedDeprecatedButtonHyperskillAnalyticEvent
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
                State.Unsupported to emptySet() // TODO: Show to user
            is Message.FetchStageImplementResult.NetworkError ->
                State.NetworkError to emptySet()
            is Message.FetchStageImplementResult.Success ->
                State.Content(message.project, message.stage, message.step) to emptySet()
            is Message.DeprecatedButtonClicked ->
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
        } ?: (state to emptySet())
}