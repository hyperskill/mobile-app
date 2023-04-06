package org.hyperskill.app.track.injection

import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track.domain.repository.TrackRepository

interface TrackDataComponent {
    val trackRepository: TrackRepository
    val trackInteractor: TrackInteractor
}