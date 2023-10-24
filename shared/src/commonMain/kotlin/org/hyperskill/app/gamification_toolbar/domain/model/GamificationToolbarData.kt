package org.hyperskill.app.gamification_toolbar.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.streaks.domain.model.StreakState

@Serializable
data class GamificationToolbarData(
    @SerialName("track_progress")
    val trackProgress: GamificationToolbarTrackProgress?,
    @SerialName("current_streak")
    val currentStreak: Int,
    @SerialName("streak_state")
    val streakState: StreakState,
    @SerialName("hypercoins_balance")
    val hypercoinsBalance: Int
)