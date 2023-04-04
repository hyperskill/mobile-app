package org.hyperskill.app.track.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.track.data.repository.TrackRepositoryImpl
import org.hyperskill.app.track.data.source.TrackRemoteDataSource
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track.domain.repository.TrackRepository
import org.hyperskill.app.track.remote.TrackRemoteDataSourceImpl

class TrackDataComponentImpl(appGraph: AppGraph) : TrackDataComponent {
    private val trackRemoteDataSource: TrackRemoteDataSource = TrackRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val trackRepository: TrackRepository = TrackRepositoryImpl(
        trackRemoteDataSource = trackRemoteDataSource,
        trackCacheDataSource = appGraph.singletonRepositoriesComponent.trackCacheDataSource
    )

    override val trackInteractor: TrackInteractor
        get() = TrackInteractor(trackRepository)
}