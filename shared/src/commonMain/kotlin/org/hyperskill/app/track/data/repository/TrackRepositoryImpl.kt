package org.hyperskill.app.track.data.repository

import org.hyperskill.app.core.data.repository_cache.RepositoryCacheProxy
import org.hyperskill.app.track.data.source.TrackCacheDataSource
import org.hyperskill.app.track.data.source.TrackRemoteDataSource
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.repository.TrackRepository

class TrackRepositoryImpl(
    private val trackRemoteDataSource: TrackRemoteDataSource,
    private val trackCacheDataSource: TrackCacheDataSource
) : TrackRepository {

    private val trackCacheProxy = RepositoryCacheProxy(
        cache = trackCacheDataSource,
        loadValuesFromRemote = { trackIds ->
            trackRemoteDataSource.getTracks(trackIds)
        },
        getKeyFromValue = { track ->
            track.id
        }
    )

    override suspend fun getTracks(trackIds: List<Long>, forceLoadFromRemote: Boolean): Result<List<Track>> =
        trackCacheProxy.getValues(trackIds, forceLoadFromRemote)

    override suspend fun getAllTracks(): Result<List<Track>> =
        trackRemoteDataSource.getAllTracks()
}