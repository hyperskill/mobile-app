package org.hyperskill.app.core.flowredux.presentation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import ru.nobird.app.core.model.Cancellable
import ru.nobird.app.presentation.redux.feature.Feature

class ReduxFlowView<State, Message, ViewAction>(
    private val feature: Feature<State, Message, ViewAction>
) : FlowView<State, Message, ViewAction>, Cancellable {
    init {
        feature.addStateListener { state ->
            this._stateFlow.value = state
        }
        feature.addActionListener { action ->
            actionChannel.trySend(action)
        }
    }

    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(feature.state)
    override val state: StateFlow<State>
        get() = _stateFlow.asStateFlow()

    private val actionChannel: Channel<ViewAction> = Channel(capacity = Channel.UNLIMITED)
    override val actions: Flow<ViewAction> by lazy {
        actionChannel.receiveAsFlow()
    }

    override fun onNewMessage(message: Message) {
        feature.onNewMessage(message)
    }

    override fun cancel() {
        feature.cancel()
        actionChannel.close()
    }
}