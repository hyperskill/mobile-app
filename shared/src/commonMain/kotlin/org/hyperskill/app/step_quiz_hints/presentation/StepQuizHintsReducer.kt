package org.hyperskill.app.step_quiz_hints.presentation

import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Action
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Message
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class StepQuizHintsReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.InitWithHintsIDs ->
                if (state is State.Idle) {
                    State.Content(message.hintsIds, null, false) to emptySet()
                } else {
                    null
                }
            is Message.HintReported ->
                if (state is State.Content && state.currentHint != null) {
                    state.copy(hintHasReaction = true) to setOf(Action.ReportHint(state.currentHint.id, state.currentHint.targetId))
                } else {
                    null
                }
            is Message.ReactionButtonClicked ->
                if (state is State.Content && state.currentHint != null) {
                    state.copy(hintHasReaction = true) to setOf(Action.ReactHint(state.currentHint.id, state.currentHint.targetId, message.reaction))
                } else {
                    null
                }
            is Message.NextHintButtonClicked ->
                if (state is State.Content) {
                    val hintsIds = state.hintsIds.toMutableList()
                    val nextHintId = hintsIds.removeLast()
                    State.Loading to setOf(Action.FetchNextHint(nextHintId, hintsIds))
                } else {
                    null
                }
            is Message.NextHintLoaded ->
                if (state is State.Loading) {
                    State.Content(message.remainingHintsIds, message.nextHint, false) to emptySet()
                } else {
                    null
                }
        } ?: (state to emptySet())
}