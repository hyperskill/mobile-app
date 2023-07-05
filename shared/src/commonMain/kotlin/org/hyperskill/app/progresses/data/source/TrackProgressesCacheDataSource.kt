package org.hyperskill.app.progress_screen.data.source

import org.hyperskill.app.core.domain.repository_cache.RepositoryCache
import org.hyperskill.app.track.domain.model.TrackProgress

interface TrackProgressesCacheDataSource : RepositoryCache<Long, TrackProgress>