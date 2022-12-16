package org.hyperskill.app.profile.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Gamification(
    @SerialName("hypercoins")
    val hypercoinsBalance: Int,
    @SerialName("passed_projects")
    val passedProjectsCount: Int = 0,
    @SerialName("passed_tracks")
    val passedTracksCount: Int = 0
)
