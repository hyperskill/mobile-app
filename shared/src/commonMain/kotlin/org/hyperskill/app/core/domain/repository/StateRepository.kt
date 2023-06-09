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
     * Update state locally in app.
     * All the subscribers of the [changes] flow get the [newState].
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

/**
 * Update the current state locally in the app using [transformState] lambda.
 * Remote state loading is used in cased current state is not cached.
 * [transformState] is not called in cased currentState fetching is failed.
 * All the subscribers of the [StateRepository.changes] flow get the new state produced by [transformState].
 */
suspend fun <State : Any> StateRepository<State>.updateState(transformState: (State) -> State) {
    val currentState = getState(forceUpdate = false).getOrNull()
    if (currentState != null) {
        val newState = transformState(currentState)
        updateState(newState)
    }
}