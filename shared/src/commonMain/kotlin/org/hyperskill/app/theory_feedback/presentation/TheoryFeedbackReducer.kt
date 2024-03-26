package org.hyperskill.app.theory_feedback.presentation

import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.Action
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.InternalAction
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.Message
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias TheoryFeedbackReducerResult = Pair<State, Set<Action>>

internal class TheoryFeedbackReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): TheoryFeedbackReducerResult =
        when (message) {
            Message.Initialize -> TODO("Not implemented")
            Message.ViewedEventMessage -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        TODO("Add analytics event")
                    )
                )
            }
        }
}