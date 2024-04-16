package org.hyperskill.app.problems_limit_reached.presentation

import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.Action
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.InternalAction
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.Message
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ProblemsLimitReachedReducerResult = Pair<State, Set<Action>>

internal class ProblemsLimitReachedModalReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ProblemsLimitReachedReducerResult =
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