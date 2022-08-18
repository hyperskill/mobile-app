package org.hyperskill.app.streak.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.max

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
    val isNewRecord: Boolean,
    @SerialName("history")
    val history: List<HistoricalStreak>
) {
    fun getStreakWithTodaySolved(): Streak =
        this.copy(
            history = this.history.mapIndexed { index, historicalStreak ->
                if (index == 0) {
                    historicalStreak.copy(
                        state = StreakState.COMPLETED
                    )
                } else {
                    historicalStreak
                }
            },
            currentStreak = this.currentStreak + 1,
            maxStreak = max(this.maxStreak, this.currentStreak + 1),
            isNewRecord = this.maxStreak <= this.currentStreak + 1
        )
}