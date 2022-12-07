package org.hyperskill.app.track.injection

import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track.presentation.TrackFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface TrackComponent {
    val trackFeature: Feature<TrackFeature.State, TrackFeature.Message, TrackFeature.Action>

    val trackInteractor: TrackInteractor
}