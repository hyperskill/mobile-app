package org.hyperskill.app.core.presentation

import ru.nobird.app.core.model.Cancellable
import ru.nobird.app.presentation.redux.feature.Feature

fun <InitialState, NewState, Message, Action> Feature<InitialState, Message, Action>.transformState(
    transformState: (InitialState) -> NewState
): Feature<NewState, Message, Action>  =
    object : Feature<NewState, Message, Action> {
        override val state: NewState
            get() = transformState(this@transformState.state)

        override fun cancel() {
            this@transformState.cancel()
        }

        override fun addActionListener(listener: (action: Action) -> Unit): Cancellable =
            this@transformState.addActionListener(listener)

        override fun addStateListener(listener: (state2: NewState) -> Unit): Cancellable =
            this@transformState.addStateListener { state1 ->
                listener(transformState(state1))
            }

        override fun onNewMessage(message: Message) {
            this@transformState.onNewMessage(message)
        }
    }