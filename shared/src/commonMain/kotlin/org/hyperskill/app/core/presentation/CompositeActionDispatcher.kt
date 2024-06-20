package org.hyperskill.app.core.presentation

import ru.nobird.app.presentation.redux.dispatcher.ActionDispatcher

abstract class CompositeActionDispatcher<Action, Message> internal constructor(
    private val dispatchers: List<ActionDispatcher<Action, Message>>
) : ActionDispatcher<Action, Message> {
    override fun setListener(listener: (message: Message) -> Unit) {
        dispatchers.forEach {
            it.setListener(listener)
        }
    }

    override fun handleAction(action: Action) {
        dispatchers.forEach {
            it.handleAction(action)
        }
    }

    override fun cancel() {
        dispatchers.forEach {
            it.cancel()
        }
    }
}