package org.hyperskill.app.step_quiz_hints.presentation

import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Action
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Message
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class StepQuizHintsReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.InitWithStepId ->
                if (state is State.Idle) {
                    State.Loading to setOf(Action.FetchHintsIds(message.stepId))
                } else {
                    null
                }
            is Message.HintsIdsLoaded ->
                if (state is State.Loading) {
                    State.Content(message.hintsIds, null, false) to emptySet()
                } else {
                    null
                }
            is Message.HintReported ->
                if (state is State.Content && state.currentHint != null) {
                    state.copy(hintHasReaction = true) to setOf(
                        Action.ReportHint(
                            hintId = state.currentHint.id,
                            stepId = state.currentHint.targetId
                        )
                    )
                } else {
                    null
                }
            is Message.ReactionButtonClicked ->
                if (state is State.Content && state.currentHint != null) {
                    state.copy(hintHasReaction = true) to setOf(
                        Action.ReactHint(
                            hintId = state.currentHint.id,
                            stepId = state.currentHint.targetId,
                            reaction = message.reaction
                        )
                    )
                } else {
                    null
                }
            is Message.LoadHintButtonClicked ->
                when (state) {
                    is State.Content -> {
                        val hintsIds = state.hintsIds.toMutableList()
                        val nextHintId = hintsIds.removeLast()
                        State.Loading to setOf(Action.FetchNextHint(nextHintId, hintsIds))
                    }
                    is State.NetworkError -> {
                        State.Loading to setOf(Action.FetchNextHint(state.nextHintId, state.hintsIds))
                    }
                    else -> {
                        null
                    }
                }
            is Message.NextHintLoaded ->
                if (state is State.Loading) {
                    State.Content(message.remainingHintsIds, message.nextHint, false) to emptySet()
                } else {
                    null
                }
            is Message.NextHintLoadingError ->
                if (state is State.Loading) {
                    State.NetworkError(message.nextHintId, message.remainingHintsIds) to emptySet()
                } else {
                    null
                }
        } ?: (state to emptySet())
}