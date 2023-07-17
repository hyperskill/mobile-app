package org.hyperskill.app.core.domain.repository

/**
 * Defines the way how to store a state
 * Default implementation is [InMemoryStateHolder]
 */
interface StateHolder<State : Any?> {
    fun getState(): State?
    fun setState(newState: State)
    fun resetState()
}