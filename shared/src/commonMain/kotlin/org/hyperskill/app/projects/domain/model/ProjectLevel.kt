package org.hyperskill.app.projects.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ProjectLevel {
    @SerialName("easy")
    EASY,
    @SerialName("medium")
    MEDIUM,
    @SerialName("hard")
    HARD,
    @SerialName("nightmare")
    NIGHTMARE
}