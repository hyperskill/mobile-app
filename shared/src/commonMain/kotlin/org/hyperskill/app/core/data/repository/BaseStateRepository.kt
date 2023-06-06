package org.hyperskill.app.core.data.repository

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.hyperskill.app.core.domain.repository.InMemoryStateHolder
import org.hyperskill.app.core.domain.repository.StateHolder
import org.hyperskill.app.core.domain.repository.StateRepository

/**
 * Thread safe repository that allows to
 * get, update, reload and subscribe to change of
 * some state between multiple app modules
 *
 * @param State type of state to store
 * @property stateHolder holder of state (in-memory, SharedPreferences etc.)
 */
abstract class BaseStateRepository<State : Any> : StateRepository<State> {
    private val mutex = Mutex()

    protected open val mutableSharedFlow = MutableSharedFlow<State>()

    protected open val stateHolder: StateHolder<State> =
        InMemoryStateHolder()

    /**
     * Flow of state changes
     *
     * @return shared flow
     */
    override val changes: SharedFlow<State>
        get() = mutableSharedFlow

    /**
     * Load state from some source
     *
     * @return result of state loading
     */
    protected abstract suspend fun loadState(): Result<State>

    /**
     * Load state if needed and return new or old value
     *
     * @param forceUpdate force loading of state
     * @return current state
     */
    override suspend fun getState(forceUpdate: Boolean): Result<State> =
        mutex.withLock {
            val currentState = stateHolder.getState()

            if (currentState != null && !forceUpdate) {
                return Result.success(currentState)
            }

            return loadAndAssignState()
        }

    /**
     * Update state locally in app
     *
     * @param newState new state to be updated
     */
    override suspend fun updateState(newState: State) {
        mutex.withLock {
            stateHolder.setState(newState)
            mutableSharedFlow.emit(newState)
        }
    }

    /**
     * Load and set actual state
     *
     * @return result of state loading
     */
    override suspend fun reloadState(): Result<State> =
        mutex.withLock {
            loadAndAssignState()
        }

    /**
     * Reset current local state
     * next call of getState will load it
     */
    override suspend fun resetState() {
        stateHolder.resetState()
    }

    /**
     * Loads state and assign it on success
     *
     * @return result of loading
     */
    private suspend fun loadAndAssignState(): Result<State> =
        loadState().onSuccess {
            stateHolder.setState(it)
            mutableSharedFlow.emit(it)
        }
}