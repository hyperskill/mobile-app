package org.hyperskill.app.track_selection.details.injection

import org.hyperskill.app.track_selection.details.domain.repository.TrackSelectionDetailsRepository

interface TrackSelectionDetailsDataComponent {
    val trackSelectionDetailsRepository: TrackSelectionDetailsRepository
}