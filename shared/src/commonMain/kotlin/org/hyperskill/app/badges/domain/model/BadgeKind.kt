package org.hyperskill.app.badges.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// The order is important, don't change it!
@Serializable
enum class BadgeKind {
    @SerialName("project master")
    ProjectMaster,
    @SerialName("topic master")
    TopicMaster,
    @SerialName("committed learner")
    CommittedLearner,
    @SerialName("brilliant mind")
    BrilliantMind,
    @SerialName("helping hand")
    HelpingHand,
    @SerialName("sweetheart")
    Sweetheart,
    @SerialName("benefactor")
    Benefactor,
    @SerialName("bounty hunter")
    BountyHunter
}