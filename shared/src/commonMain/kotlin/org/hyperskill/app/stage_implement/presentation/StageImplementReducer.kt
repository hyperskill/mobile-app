package org.hyperskill.app.stage_implement.presentation

import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Action
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Message
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal class StageImplementReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize -> {
                if (state is State.Idle ||
                    (message.forceUpdate && state is State.NetworkError)
                ) {
                    State.Loading to setOf(
                        Action.FetchStageImplement(projectId = message.projectId, stageId = message.stageId),
                        // TODO: View event
                    )
                } else {
                    null
                }
            }
            is Message.FetchStageImplementResult.Deprecated ->
                State.Deprecated to emptySet() // TODO: Show to user
            is Message.FetchStageImplementResult.Unsupported ->
                State.Unsupported to emptySet() // TODO: Show to user
            is Message.FetchStageImplementResult.NetworkError ->
                State.NetworkError to emptySet()
            is Message.FetchStageImplementResult.Success ->
                State.Content(message.project, message.stage, message.step) to emptySet()
        } ?: (state to emptySet())
}