package org.hyperskill.app.core.domain.repository

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class SingleEntityRepository<State> {
    private var state: State? = null

    private val mutex = Mutex()

    private val mutableSharedFlow = MutableSharedFlow<State>()

    protected abstract suspend fun loadState(): Result<State>

    /**
     * Load state if needed and return in-memory value
     *
     * @return result of state loading or in-memory value
     */
    suspend fun getState(): Result<State> =
        mutex.withLock {
            val currentState = state

            if (currentState != null) {
                return Result.success(currentState)
            }

            return loadAndAssignState()
        }

    /**
     * Flow of state changes
     *
     * @return shared flow
     */
    fun sharedFlow(): SharedFlow<State> =
        mutableSharedFlow

    /**
     * Update state locally in app
     *
     * @param newState new state to be updated
     */
    suspend fun updateState(newState: State) {
        mutex.withLock {
            state = newState
            mutableSharedFlow.emit(newState)
        }
    }

    /**
     * Load and set actual state
     *
     * @return result of state loading
     */
    suspend fun reloadState(): Result<State> {
        mutex.withLock {
            return loadAndAssignState()
        }
    }

    /**
     * Loads state and assign it on success
     *
     * @return result of loading
     */
    private suspend fun loadAndAssignState(): Result<State> =
        loadState().onSuccess {
            state = it
            mutableSharedFlow.emit(it)
        }
}