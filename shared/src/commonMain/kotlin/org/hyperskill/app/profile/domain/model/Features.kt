package org.hyperskill.app.profile.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Features(
    @SerialName("learning_path.diagnostic")
    val learningPathDiagnostic: Boolean,
//    @SerialName("frontend.confirm_email_toast")
//    val frontendConfirmEmailToast: Boolean,
    @SerialName("topics.repetition")
    val topicsRepetition: Boolean,
    @SerialName("gamification_badges")
    val gamificationBadges: Boolean,
    @SerialName("backend.track_activities")
    val backendTrackActivities: Boolean,
    @SerialName("stages_progress_indicators")
    val stagesProgressIndicatos: Boolean,
    @SerialName("settings.isic")
    val settingsIsic: Boolean,
    @SerialName("new_profile.tracks")
    val newProfileTracks: Boolean,
//    @SerialName("metrics.frontend_events")
//    val metricsFrontendEvents: Boolean,
    @SerialName("registration.simplified_form")
    val registrationSimplifiedForm: Boolean,
    @SerialName("knowledge_map.next_to_learn_topic")
    val knowledgeMapNextToLearnTopic: Boolean,
    @SerialName("study_plan.stack_completed_activities")
    val studyPlanStackCompletedActivities: Boolean,
    @SerialName("frontend.kindergarten")
    val frontendKindergarten: Boolean,
    @SerialName("projects.capstone_project")
    val projectsCapstoneProject: Boolean,
    @SerialName("header.streak_dropdown")
    val headerStreakDropdown: Boolean,
    @SerialName("project_knowledge_map")
    val projectKnowledgeMap: Boolean,
    @SerialName("header.track_dashboard")
    val headerTrackDashboard: Boolean,
    @SerialName("ui.capstone_projects")
    val uiCapstoneProjects: Boolean,
    @SerialName("users.disabled_landing_redirect")
    val usersDisabledLandingRedirect: Boolean,
    @SerialName("steps.dataset_in_ide")
    val stepsDatasetInIDE: Boolean,
    @SerialName("study_plan.project_feedback")
    val studyPlayProjectFeedback: Boolean,
    @SerialName("streaks.freeze_token_enabled")
    val streaksFreezeTokenEnabled: Boolean,
    @SerialName("exp.hide_diagnostics_results")
    val expHideDiagnosticsResults: Boolean,
    @SerialName("exp.hide_diagnostics_feedback")
    val expHideDiagnosticsFeedback: Boolean,
    @SerialName("track_landing.personal_plan_info")
    val trackLandingPersonalPlanInfo: Boolean,
    @SerialName("track.new_card_design")
    val trackNewCardDesign: Boolean,
    @SerialName("progress.use_ws_only")
    val progressUseWsOnly: Boolean,
    @SerialName("project.new_card_design")
    val projectNewCardDesign: Boolean,
    @SerialName("study_plan.track_feedback")
    val studyPlanTrackFeedback: Boolean,
//    @SerialName("discord_study_groups")
//    val discordStudyGroups: Boolean
)
