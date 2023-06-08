package org.hyperskill.app.providers.domain.repository

import org.hyperskill.app.providers.domain.model.Provider

interface ProvidersRepository {
    suspend fun getProviders(providersIds: List<Long>, forceLoadFromRemote: Boolean): Result<List<Provider>>

    suspend fun getProvider(providerId: Long, forceLoadFromRemote: Boolean): Result<Provider> =
        kotlin.runCatching {
            getProviders(listOf(providerId), forceLoadFromRemote).getOrThrow().first()
        }

    fun clearCache()
}