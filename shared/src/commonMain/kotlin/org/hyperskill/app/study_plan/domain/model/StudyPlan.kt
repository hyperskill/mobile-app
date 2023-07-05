package org.hyperskill.app.study_plan.domain.model

import kotlin.math.roundToInt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    val secondsToReachTrack: Float,
    @SerialName("seconds_to_reach_project")
    val secondsToReachProject: Float,
    @SerialName("status")
    val status: StudyPlanStatus
) {
    @Deprecated("Use SharedDateFormatter instead, remove after Track feature removal")
    val minutesToReachTrack: Int =
        (secondsToReachTrack / 60.0).roundToInt()

    @Deprecated("Use SharedDateFormatter instead, remove after Track feature removal")
    val hoursToReachTrack: Int =
        (secondsToReachTrack / 3600.0).roundToInt()
}