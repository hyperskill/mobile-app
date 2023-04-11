package org.hyperskill.app.track.cache

import org.hyperskill.app.core.data.repository_cache.InMemoryRepositoryCache
import org.hyperskill.app.core.domain.repository_cache.RepositoryCache
import org.hyperskill.app.track.data.source.TrackCacheDataSource
import org.hyperskill.app.track.domain.model.Track

class TrackCacheDataSourceImpl(
    cache: InMemoryRepositoryCache<Long, Track>
) : TrackCacheDataSource, RepositoryCache<Long, Track> by cache