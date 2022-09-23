package org.hyperskill.app.track.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.track.data.repository.TrackRepositoryImpl
import org.hyperskill.app.track.data.source.TrackRemoteDataSource
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track.domain.repository.TrackRepository
import org.hyperskill.app.track.presentation.TrackFeature
import org.hyperskill.app.track.remote.TrackRemoteDataSourceImpl
import ru.nobird.app.presentation.redux.feature.Feature

class TrackComponentImpl(private val appGraph: AppGraph) : TrackComponent {
    private val trackRemoteDataSource: TrackRemoteDataSource = TrackRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val trackRepository: TrackRepository = TrackRepositoryImpl(trackRemoteDataSource)
    private val trackInteractor: TrackInteractor = TrackInteractor(trackRepository)

    override val trackFeature: Feature<TrackFeature.State, TrackFeature.Message, TrackFeature.Action>
        get() = TrackFeatureBuilder.build(
            trackInteractor,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.analyticComponent.analyticInteractor
        )
}