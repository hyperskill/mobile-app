package org.hyperskill.app.providers.cache

import org.hyperskill.app.core.data.repository_cache.InMemoryRepositoryCache
import org.hyperskill.app.core.domain.repository_cache.RepositoryCache
import org.hyperskill.app.providers.data.source.ProvidersCacheDataSource
import org.hyperskill.app.providers.domain.model.Provider

class ProvidersCacheDataSourceImpl(
    cache: InMemoryRepositoryCache<Long, Provider>
) : ProvidersCacheDataSource, RepositoryCache<Long, Provider> by cache