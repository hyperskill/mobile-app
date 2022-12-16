package org.hyperskill.app.core.presentation

import ru.nobird.app.core.model.Cancellable
import ru.nobird.app.presentation.redux.feature.Feature

fun <State1, State2, Message, Action> Feature<State1, Message, Action>.transformState(
    transformState: (State1) -> State2
): Feature<State2, Message, Action>  =
    object : Feature<State2, Message, Action> {
        override val state: State2
            get() = transformState(this@transformState.state)

        override fun cancel() {
            this@transformState.cancel()
        }

        override fun addActionListener(listener: (action: Action) -> Unit): Cancellable {
            return this@transformState.addActionListener(listener)
        }

        override fun addStateListener(listener: (state2: State2) -> Unit): Cancellable {
            return this@transformState.addStateListener { state1 ->
                listener(transformState(state1))
            }
        }

        override fun onNewMessage(message: Message) {
            this@transformState.onNewMessage(message)
        }
    }