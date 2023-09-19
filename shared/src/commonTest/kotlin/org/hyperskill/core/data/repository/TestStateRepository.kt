package org.hyperskill.core.data.repository

import org.hyperskill.app.core.data.repository.BaseStateRepository
import org.hyperskill.app.core.domain.repository.InMemoryStateHolder
import org.hyperskill.app.core.domain.repository.StateHolder

class TestStateRepository(
    override val stateHolder: StateHolder<String> = InMemoryStateHolder(),
    private val externalLoadState: suspend () -> String = { DEFAULT_RESPONSE }
) : BaseStateRepository<String>() {

    companion object {
        const val DEFAULT_RESPONSE = "DefaultResponse"
    }

    override suspend fun loadState(): Result<String> {
        val result = externalLoadState()
        return Result.success(result)
    }
}