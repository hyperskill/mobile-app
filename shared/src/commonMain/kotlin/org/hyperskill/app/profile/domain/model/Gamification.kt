package org.hyperskill.app.profile.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Gamification(
    @SerialName("topics_repetitions")
    val topicsRepetitions: GamificationTopicsRepetitions,
    @SerialName("hypercoins")
    val hypercoins: Int,
)
