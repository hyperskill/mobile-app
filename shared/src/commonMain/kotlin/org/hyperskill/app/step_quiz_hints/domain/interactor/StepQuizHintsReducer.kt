package org.hyperskill.app.step_quiz_hints.domain.interactor

import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Action
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Message
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class StepQuizHintsReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.InitWithHintsIDs ->
                if (state is State.Idle) {
                    State.Content(message.hintsIDs, null, false) to emptySet()
                } else {
                    null
                }
            is Message.HintReported ->
                if (state is State.Content && state.currentHint != null) {
                    state.copy(hintHasReaction = true) to setOf(Action.ReportHint(state.currentHint.id, state.currentHint.targetID))
                } else {
                    null
                }
            is Message.ReactionButtonClicked ->
                if (state is State.Content && state.currentHint != null) {
                    state.copy(hintHasReaction = true) to setOf(Action.ReactHint(state.currentHint.id, state.currentHint.targetID, message.reaction))
                } else {
                    null
                }
            is Message.NextHintButtonClicked ->
                if (state is State.Content) {
                    val hintsIDs = state.hintsIDs.toMutableList()
                    val nextHintID = hintsIDs.removeLast()
                    State.Loading to setOf(Action.FetchNextHint(nextHintID, hintsIDs))
                } else {
                    null
                }
            is Message.NextHintLoaded ->
                if (state is State.Loading) {
                    State.Content(message.remainingHintsIDs, message.nextHint, false) to emptySet()
                } else {
                    null
                }
        } ?: (state to emptySet())
}