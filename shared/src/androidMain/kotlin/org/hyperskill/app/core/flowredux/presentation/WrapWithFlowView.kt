package org.hyperskill.app.core.flowredux.presentation

import ru.nobird.app.core.model.Cancellable
import ru.nobird.app.presentation.redux.feature.Feature

inline fun <State, Message, Action, reified ViewAction : Action> Feature<State, Message, Action>.wrapWithFlowView(): FlowView<State, Message, ViewAction> {
    val feature = object : Feature<State, Message, ViewAction> {
        override val state: State
            get() = this@wrapWithFlowView.state

        override fun addActionListener(listener: (action: ViewAction) -> Unit): Cancellable =
            this@wrapWithFlowView.addActionListener { (it as? ViewAction)?.let(listener) }

        override fun addStateListener(listener: (state: State) -> Unit): Cancellable =
            this@wrapWithFlowView.addStateListener(listener)

        override fun onNewMessage(message: Message) {
            this@wrapWithFlowView.onNewMessage(message)
        }

        override fun cancel() {
            this@wrapWithFlowView.cancel()
        }
    }
    return ReduxFlowView(feature)
}