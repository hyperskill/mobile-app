package org.hyperskill.app.track.injection

import org.hyperskill.app.track.domain.interactor.TrackInteractor

interface TrackDataComponent {
    val trackInteractor: TrackInteractor
}