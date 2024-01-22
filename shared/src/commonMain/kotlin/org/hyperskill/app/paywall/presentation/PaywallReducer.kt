package org.hyperskill.app.paywall.presentation

import org.hyperskill.app.paywall.presentation.PaywallFeature.Action
import org.hyperskill.app.paywall.presentation.PaywallFeature.Message
import org.hyperskill.app.paywall.presentation.PaywallFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias PaywallReducerResult = Pair<State, Set<Action>>

class PaywallReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): PaywallReducerResult =
        when (message) {
            Message.ViewedEventMessage -> TODO()
        }
}