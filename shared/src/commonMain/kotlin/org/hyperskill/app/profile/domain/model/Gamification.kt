package org.hyperskill.app.profile.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Gamification(
//    @SerialName("active_days")
//    val activeDays: Int,
//    @SerialName("daily_step_completed_count")
//    val dailyStepCompletedCount: Int,
//    @SerialName("passed_problems")
//    val passedProblems: Int,
//    @SerialName("passed_projects")
//    val passedProjects: Int,
//    @SerialName("passed_topics")
//    val passedTopics: Int,
//    @SerialName("progress_updated_at")
//    val progressUpdatedAt: String?,
//    @SerialName("activities_count")
//    val activitiesCount: Int,
//    @SerialName("passed_activities")
//    val passedActivities: Int,
//    @SerialName("hypercoins")
//    val hypercoins: Long,
//    @SerialName("last_code_problem_client") // web - String
//    val lastCodeProblemClient: Long?,
//    @SerialName("notifications_unread")
//    val notificationsUnread: Int,
//    @SerialName("passed_problems_in_ide")
//    val passedProblemsInIDE: Int,
//    @SerialName("passed_stages")
//    val passedStages: Int,
//    @SerialName("passed_theories")
//    val passedTheories: Int,
//    @SerialName("seconds_to_reach_stage")
//    val secondsToReachStage: Long,
    @SerialName("topics_repetitions")
    val topicsRepetitions: GamificationTopicsRepetitions
)
