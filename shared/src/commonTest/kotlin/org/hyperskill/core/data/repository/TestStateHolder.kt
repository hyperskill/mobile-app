package org.hyperskill.core.data.repository

import kotlin.time.Duration
import kotlinx.coroutines.delay
import org.hyperskill.app.core.domain.repository.StateHolder

class TestStateHolder<State : Any?>(
    private val readStateDelay: Duration
) : StateHolder<State> {
    private var state: State? = null

    override suspend fun getState(): State? {
        delay(readStateDelay)
        return state
    }

    override suspend fun setState(newState: State) {
        this.state = newState
    }

    override fun resetState() {
        state = null
    }
}