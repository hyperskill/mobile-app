package org.hyperskill.app.core.flowredux.presentation

import androidx.lifecycle.ViewModel

abstract class ReduxFlowViewModel<State, Message, ViewAction>(
    private val viewContainer: FlowView<State, Message, ViewAction>
) : ViewModel(), FlowView<State, Message, ViewAction> by viewContainer {
    override fun onCleared() {
        viewContainer.cancel()
    }
}