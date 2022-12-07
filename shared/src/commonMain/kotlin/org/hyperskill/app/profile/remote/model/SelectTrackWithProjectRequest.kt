package org.hyperskill.app.profile.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SelectTrackWithProjectRequest(
    @SerialName("track_id")
    val trackId: Long,
    @SerialName("project")
    val project: Long
)
