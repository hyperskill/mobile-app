package org.hyperskill.app.notification.click_handling.presentation

import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.Action
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.Message
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class NotificationClickHandlingReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> {
        when (message) {
            else -> TODO("Not implemented yet")
        }
    }
}