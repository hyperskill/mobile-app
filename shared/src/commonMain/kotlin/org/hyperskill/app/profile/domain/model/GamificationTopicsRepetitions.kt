package org.hyperskill.app.profile.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GamificationTopicsRepetitions(
    @SerialName("repetitions_count")
    val repetitionsCount: Int? = null,
    @SerialName("repeated_today_count")
    val repeatedTodayCount: Int? = null
)
