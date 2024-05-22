package org.hyperskill.app.challenges.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Challenge(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("target_type")
    internal val targetTypeValue: String,
    @SerialName("start")
    val start: Instant,
    @SerialName("end")
    val end: Instant,
    @SerialName("intervals_count")
    val intervalsCount: Int,
    @SerialName("status")
    internal val statusValue: String,
    @SerialName("reward_link")
    val rewardLink: String?,
    @SerialName("progress")
    val progress: List<Boolean>,
    @SerialName("current_interval")
    val currentInterval: Int?,
    @SerialName("next_interval_time")
    val nextIntervalTime: Instant?
) {
    val targetType: ChallengeTargetType?
        get() = ChallengeTargetType.getByValue(targetTypeValue)

    val status: ChallengeStatus?
        get() = ChallengeStatus.getByValue(statusValue)
}