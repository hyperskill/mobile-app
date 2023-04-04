package org.hyperskill.app.progresses.data.source

import org.hyperskill.app.core.data.repository_cache.RepositoryCache
import org.hyperskill.app.track.domain.model.TrackProgress

interface TrackProgressesCacheDataSource : RepositoryCache<Long, TrackProgress>