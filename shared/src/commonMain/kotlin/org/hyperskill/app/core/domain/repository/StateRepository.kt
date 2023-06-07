package org.hyperskill.app.core.domain.repository

import kotlinx.coroutines.flow.SharedFlow

interface StateRepository<State : Any> {
    /**
     * Load state if needed and return cached or remote-fetched value
     *
     * @param forceUpdate force loading of state from remote
     * @return current state
     */
    suspend fun getState(forceUpdate: Boolean = false): Result<State>

    /**
     * Load state if needed and return cached or remote-fetched value
     *
     * @param forceUpdate force loading of state from remote
     * @return [StateWithSource] with currentState to know the DataSourceType witch was used to get the state.
     * @see [StateWithSource]
     */
    suspend fun getStateWithSource(forceUpdate: Boolean = false): Result<StateWithSource<State>>

    /**
     * Flow of state changes
     *
     * @return shared flow
     */
    val changes: SharedFlow<State>

    /**
     * Update state locally in app
     *
     * @param newState new state to be updated
     */
    suspend fun updateState(newState: State)

    /**
     * Load and set actual state
     *
     * @return result of state loading
     */
    suspend fun reloadState(): Result<State>

    /**
     * Reset current local state
     * next call of getState will load it
     *
     */
    suspend fun resetState()
}

suspend fun <State : Any> StateRepository<State>.updateState(block: (State) -> State) {
    val currentState = getState(forceUpdate = false).getOrNull()
    if (currentState != null) {
        val newState = block(currentState)
        updateState(newState)
    }
}