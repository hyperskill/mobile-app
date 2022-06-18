package org.hyperskill.app.profile.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicsRepetitions(
    @SerialName("repetitions_count")
    val repetitionsCount: Int,
    @SerialName("repeated_today_count")
    val repeatedTodayCount: Int
)
