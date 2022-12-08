package org.hyperskill.app.track.domain.model

import kotlin.math.roundToInt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackProgress(
    @SerialName("id")
    val id: String,
    @SerialName("vid")
    val vid: String,
    @SerialName("clarity")
    val clarity: Float?,
    @SerialName("fun")
    val funMeasure: Float?,
    @SerialName("usefulness")
    val usefulness: Float?,
    @SerialName("completed_projects")
    val completedProjects: List<Long>,
    @SerialName("completed_capstone_projects")
    val completedCapstoneProjects: List<Long>,
    @SerialName("applied_capstone_topics_count")
    val appliedCapstoneTopicsCount: Int,
    @SerialName("is_completed")
    val isCompleted: Boolean,
    @SerialName("learned_topics_count")
    val learnedTopicsCount: Int,
    @SerialName("skipped_topics_count")
    val skippedTopicsCount: Int
) {
    val averageRating: Double
        get() {
            val internalFunMeasure = (funMeasure ?: 0).toDouble()
            val internalClarity = (clarity ?: 0).toDouble()
            val internalUsefulness = (usefulness ?: 0).toDouble()
            val avgRating = (internalFunMeasure + internalClarity + internalUsefulness) / 3

            return (avgRating * 10).roundToInt() / 10.0
        }

    val completedTopics: Int
        get() = learnedTopicsCount + skippedTopicsCount
}
