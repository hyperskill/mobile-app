package org.hyperskill.app.manage_subscription.presentation

import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Action
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Message
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ManageSubscriptionReducerResult = Pair<State, Set<Action>>

class ManageSubscriptionReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ManageSubscriptionReducerResult =
        when (message) {
            Message.Initialize -> TODO()
            Message.ViewedEventMessage -> TODO()
        }
}