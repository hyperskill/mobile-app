package org.hyperskill.app.core.flowredux.presentation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.nobird.app.core.model.Cancellable

interface FlowView<State, Message, ViewAction> : Cancellable {
    val state: StateFlow<State>
    val actions: Flow<ViewAction>
    fun onNewMessage(message: Message)
}