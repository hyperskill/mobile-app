package org.hyperskill.app.track.injection

import org.hyperskill.app.core.data.repository_cache.InMemoryRepositoryCache
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.track.cache.TrackCacheDataSourceImpl
import org.hyperskill.app.track.data.repository.TrackRepositoryImpl
import org.hyperskill.app.track.data.source.TrackCacheDataSource
import org.hyperskill.app.track.data.source.TrackRemoteDataSource
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track.domain.repository.TrackRepository
import org.hyperskill.app.track.remote.TrackRemoteDataSourceImpl

class TrackDataComponentImpl(appGraph: AppGraph) : TrackDataComponent {
    companion object {
        private val trackCacheDataSource: TrackCacheDataSource by lazy {
            TrackCacheDataSourceImpl(InMemoryRepositoryCache())
        }
    }

    private val trackRemoteDataSource: TrackRemoteDataSource = TrackRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )

    override val trackRepository: TrackRepository
        get() = TrackRepositoryImpl(
            trackRemoteDataSource = trackRemoteDataSource,
            trackCacheDataSource = trackCacheDataSource
        )

    override val trackInteractor: TrackInteractor
        get() = TrackInteractor(trackRepository)
}