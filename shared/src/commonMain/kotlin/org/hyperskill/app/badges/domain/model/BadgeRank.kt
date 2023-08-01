package org.hyperskill.app.badges.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BadgeRank {
    @SerialName("Locked")
    LOCKED,
    @SerialName("Apprentice")
    APPRENTICE,
    @SerialName("Expert")
    EXPERT,
    @SerialName("Master")
    MASTER,
    @SerialName("Legendary")
    LEGENDARY,

    UNKNOWN
}