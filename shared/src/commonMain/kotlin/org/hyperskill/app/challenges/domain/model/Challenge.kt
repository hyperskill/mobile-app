package org.hyperskill.app.challenges.domain.model

import kotlinx.datetime.LocalDate
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
    @SerialName("starting_date")
    val startingDate: LocalDate,
    @SerialName("interval_duration_days")
    val intervalDurationDays: Int,
    @SerialName("intervals_count")
    val intervalsCount: Int,
    @SerialName("status")
    internal val statusValue: String,
    @SerialName("reward_link")
    val rewardLink: String?,
    @SerialName("progress")
    val progress: List<Boolean>,
    @SerialName("finish_date")
    val finishDate: LocalDate,
    @SerialName("current_interval")
    val currentInterval: Int?
) {
    val targetType: ChallengeTargetType?
        get() = ChallengeTargetType.getByValue(targetTypeValue)

    val status: ChallengeStatus?
        get() = ChallengeStatus.getByValue(statusValue)
}