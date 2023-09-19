package org.hyperskill.app.core.domain.repository

/**
 * Defines the way how to store a state
 * Default implementation is [InMemoryStateHolder]
 */
interface StateHolder<State : Any?> {
    suspend fun getState(): State?
    suspend fun setState(newState: State)
    fun resetState()
}