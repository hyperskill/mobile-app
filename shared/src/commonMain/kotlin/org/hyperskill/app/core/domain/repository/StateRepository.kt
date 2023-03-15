package org.hyperskill.app.core.domain.repository

import kotlinx.coroutines.flow.SharedFlow

interface StateRepository<State : Any> {
    /**
     * Load state if needed and return new or old value
     *
     * @return current state
     */
    suspend fun getState(): Result<State>

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
}