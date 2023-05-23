package org.hyperskill.app.providers.data.source

import org.hyperskill.app.core.domain.repository_cache.RepositoryCache
import org.hyperskill.app.providers.domain.model.Provider

interface ProvidersCacheDataSource : RepositoryCache<Long, Provider>