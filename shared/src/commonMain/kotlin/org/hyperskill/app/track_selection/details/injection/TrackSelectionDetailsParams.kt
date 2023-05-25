package org.hyperskill.app.track_selection.details.injection

import kotlinx.serialization.Serializable
import org.hyperskill.app.track.domain.model.TrackWithProgress

@Serializable
data class TrackSelectionDetailsParams(
    val trackWithProgress: TrackWithProgress,
    val isTrackSelected: Boolean
)