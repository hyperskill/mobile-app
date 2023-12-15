package org.hyperskill.app.core.view

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel

fun <State, Message, ViewAction> ReduxFlowViewModel<State, Message, ViewAction>.handleActions(
    lifecycleOwner: LifecycleOwner,
    onAction: (ViewAction) -> Unit,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
) {
    actions
        .flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState)
        .onEach(onAction)
        .launchIn(lifecycleOwner.lifecycleScope)
}