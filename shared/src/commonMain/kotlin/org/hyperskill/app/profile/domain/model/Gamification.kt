package org.hyperskill.app.profile.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Gamification(
    @SerialName("hypercoins")
    val hypercoinsBalance: Int = 0,
    @SerialName("passed_projects")
    val passedProjectsCount: Int = 0
)
