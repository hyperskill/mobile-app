package org.hyperskill.app.track_selection.list.injection

import kotlinx.serialization.Serializable

@Serializable
data class TrackSelectionListParams(
    val isNewUserMode: Boolean = false
)