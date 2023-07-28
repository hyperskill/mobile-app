package org.hyperskill.app.core.domain.repository

/**
 * State holder which stores the state in-memory
 */
class InMemoryStateHolder<State : Any?> : StateHolder<State> {
    private var state: State? = null

    override fun getState(): State? = state

    override fun setState(newState: State) {
        this.state = newState
    }

    override fun resetState() {
        state = null
    }
}