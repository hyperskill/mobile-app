package org.hyperskill.app.track.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
data class StudyPlan(
    @SerialName("id")
    val id: Long,
    @SerialName("track_id")
    val trackId: Long?,
    @SerialName("project_id")
    val projectId: Long?,
    @SerialName("sections")
    val sections: List<Long>,
    @SerialName("seconds_to_reach_track")
    val secondsToReachTrack: Long,
    @SerialName("seconds_to_reach_project")
    val secondsToReachProject: Long,
    @SerialName("created_at")
    val createdAt: String
) {
    val minutesToReachTrack: Int =
        (secondsToReachTrack.toDouble() / 60).roundToInt()

    val hoursToReachTrack: Int =
        (secondsToReachTrack.toDouble() / 3600).roundToInt()
}
