package org.hyperskill.app.topics_repetitions.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicRepetitionStatistics(
    @SerialName("recommend_today_count")
    val recommendTodayCount: Int,
    @SerialName("repeated_today_count")
    val repeatedTodayCount: Int,
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("repeated_total_by_count")
    val repeatedTotalByCount: Map<String, Int>
)
