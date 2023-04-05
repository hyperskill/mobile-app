package org.hyperskill.app.track.data.source

import org.hyperskill.app.core.domain.repository_cache.RepositoryCache
import org.hyperskill.app.track.domain.model.Track

interface TrackCacheDataSource : RepositoryCache<Long, Track>