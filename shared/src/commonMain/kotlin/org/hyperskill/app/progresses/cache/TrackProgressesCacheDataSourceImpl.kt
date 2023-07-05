package org.hyperskill.app.progress_screen.cache

import org.hyperskill.app.core.data.repository_cache.InMemoryRepositoryCache
import org.hyperskill.app.core.domain.repository_cache.RepositoryCache
import org.hyperskill.app.progress_screen.data.source.TrackProgressesCacheDataSource
import org.hyperskill.app.track.domain.model.TrackProgress

class TrackProgressesCacheDataSourceImpl(
    cache: InMemoryRepositoryCache<Long, TrackProgress>
) : TrackProgressesCacheDataSource, RepositoryCache<Long, TrackProgress> by cache