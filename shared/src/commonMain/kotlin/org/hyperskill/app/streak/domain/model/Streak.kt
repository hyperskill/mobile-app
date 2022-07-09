package org.hyperskill.app.streak.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Streak(
    @SerialName("user_id")
    val userId: Long,
    @SerialName("kind")
    val kind: String,
    @SerialName("current_streak")
    val currentStreak: Int,
    @SerialName("max_streak")
    val maxStreak: Int,
    @SerialName("is_new_record")
    val isNewRecord: Boolean
) {
    companion object Kind {
        const val COMPLETED = "completed"
        const val MANUAL_COMPLETED = "manual_completed"
        const val FROZEN = "frozen"
    }
}