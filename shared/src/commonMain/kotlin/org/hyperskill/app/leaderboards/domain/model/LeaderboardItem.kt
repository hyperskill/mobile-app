package org.hyperskill.app.leaderboards.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.users.domain.model.User

@Serializable
data class LeaderboardItem(
    @SerialName("user")
    val user: User,
    @SerialName("position")
    val position: Int,
    @SerialName("passed_problems")
    val passedProblems: Int
)