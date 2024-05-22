package org.hyperskill.app.topic_completed_modal.presentation

import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.Action
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.InternalAction
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.Message
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias TopicCompletedModalReducerResult = Pair<State, Set<Action>>

internal class TopicCompletedModalReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): TopicCompletedModalReducerResult =
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