package org.hyperskill.app.gamification_toolbar.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GamificationToolbarTrackProgress(
    @SerialName("is_completed")
    val isCompleted: Boolean,
    @SerialName("average_progress")
    val averageProgress: Int
)