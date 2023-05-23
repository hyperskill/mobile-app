package org.hyperskill.app.providers.injection

import org.hyperskill.app.core.data.repository_cache.InMemoryRepositoryCache
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.providers.cache.ProvidersCacheDataSourceImpl
import org.hyperskill.app.providers.data.repository.ProvidersRepositoryImpl
import org.hyperskill.app.providers.data.source.ProvidersCacheDataSource
import org.hyperskill.app.providers.data.source.ProvidersRemoteDataSource
import org.hyperskill.app.providers.domain.interactor.ProvidersInteractor
import org.hyperskill.app.providers.domain.repository.ProvidersRepository
import org.hyperskill.app.providers.remote.ProvidersRemoteDataSourceImpl

class ProvidersDataComponentImpl(appGraph: AppGraph) : ProvidersDataComponent {
    companion object {
        private val providersCacheDataSource: ProvidersCacheDataSource by lazy {
            ProvidersCacheDataSourceImpl(InMemoryRepositoryCache())
        }
    }

    private val providersRemoteDataSource: ProvidersRemoteDataSource =
        ProvidersRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    override val providersRepository: ProvidersRepository
        get() = ProvidersRepositoryImpl(providersRemoteDataSource, providersCacheDataSource)

    override val providersInteractor: ProvidersInteractor
        get() = ProvidersInteractor(providersRepository)
}