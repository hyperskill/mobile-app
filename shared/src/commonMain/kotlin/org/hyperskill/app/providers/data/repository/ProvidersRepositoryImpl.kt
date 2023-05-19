package org.hyperskill.app.providers.data.repository

import org.hyperskill.app.core.domain.repository_cache.RepositoryCacheProxy
import org.hyperskill.app.providers.data.source.ProvidersCacheDataSource
import org.hyperskill.app.providers.data.source.ProvidersRemoteDataSource
import org.hyperskill.app.providers.domain.model.Provider
import org.hyperskill.app.providers.domain.repository.ProvidersRepository

class ProvidersRepositoryImpl(
    private val providersRemoteDataSource: ProvidersRemoteDataSource,
    providersCacheDataSource: ProvidersCacheDataSource
) : ProvidersRepository {
    private val providersCacheProxy = RepositoryCacheProxy(
        cache = providersCacheDataSource,
        loadValuesFromRemote = { providersIds ->
            providersRemoteDataSource.getProviders(providersIds)
        },
        getKeyFromValue = { provider ->
            provider.id
        }
    )

    override suspend fun getProviders(
        providersIds: List<Long>,
        forceLoadFromRemote: Boolean
    ): Result<List<Provider>> =
        providersCacheProxy.getValues(providersIds, forceLoadFromRemote)

    override fun clearCache() {
        providersCacheProxy.clearCache()
    }
}