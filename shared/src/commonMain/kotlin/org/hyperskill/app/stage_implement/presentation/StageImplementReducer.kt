package org.hyperskill.app.stage_implement.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
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
            is Message.FetchStageImplementResult.NetworkError ->
                State.NetworkError to emptySet()
            is Message.FetchStageImplementResult.Success ->
                State.Content(message.projectId, message.stage) to emptySet()
            is Message.ViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(StageImplementViewedHyperskillAnalyticEvent(analyticRoute)))
        } ?: (state to emptySet())
}