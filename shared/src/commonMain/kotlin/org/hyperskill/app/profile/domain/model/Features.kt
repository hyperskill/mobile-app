package org.hyperskill.app.profile.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Features(
    @SerialName("learning_path.diagnostic")
    val learningPathDiagnostic: Boolean? = null,
    @SerialName("frontend.confirm_email_toast")
    val frontendConfirmEmailToast: Boolean? = null,
    @SerialName("topics.repetition")
    val topicsRepetition: Boolean? = null,
    @SerialName("gamification_badges")
    val gamificationBadges: Boolean? = null,
    @SerialName("backend.track_activities")
    val backendTrackActivities: Boolean? = null,
    @SerialName("stages_progress_indicators")
    val stagesProgressIndicatos: Boolean? = null,
    @SerialName("settings.isic")
    val settingsIsic: Boolean? = null,
    @SerialName("new_profile.tracks")
    val newProfileTracks: Boolean? = null,
    @SerialName("metrics.frontend_events")
    val metricsFrontendEvents: Boolean? = null,
    @SerialName("registration.simplified_form")
    val registrationSimplifiedForm: Boolean? = null,
    @SerialName("knowledge_map.next_to_learn_topic")
    val knowledgeMapNextToLearnTopic: Boolean? = null,
    @SerialName("study_plan.stack_completed_activities")
    val studyPlanStackCompletedActivities: Boolean? = null,
    @SerialName("frontend.kindergarten")
    val frontendKindergarten: Boolean? = null,
    @SerialName("projects.capstone_project")
    val projectsCapstoneProject: Boolean? = null,
    @SerialName("header.streak_dropdown")
    val headerStreakDropdown: Boolean? = null,
    @SerialName("project_knowledge_map")
    val projectKnowledgeMap: Boolean? = null,
    @SerialName("header.track_dashboard")
    val headerTrackDashboard: Boolean? = null,
    @SerialName("ui.capstone_projects")
    val uiCapstoneProjects: Boolean? = null,
    @SerialName("users.disabled_landing_redirect")
    val usersDisabledLandingRedirect: Boolean? = null,
    @SerialName("steps.dataset_in_ide")
    val stepsDatasetInIDE: Boolean? = null,
    @SerialName("study_plan.project_feedback")
    val studyPlayProjectFeedback: Boolean? = null,
    @SerialName("streaks.freeze_token_enabled")
    val streaksFreezeTokenEnabled: Boolean? = null,
    @SerialName("exp.hide_diagnostics_results")
    val expHideDiagnosticsResults: Boolean? = null,
    @SerialName("exp.hide_diagnostics_feedback")
    val expHideDiagnosticsFeedback: Boolean? = null,
    @SerialName("track_landing.personal_plan_info")
    val trackLandingPersonalPlanInfo: Boolean? = null,
    @SerialName("track.new_card_design")
    val trackNewCardDesign: Boolean? = null,
    @SerialName("progress.use_ws_only")
    val progressUseWsOnly: Boolean? = null,
    @SerialName("project.new_card_design")
    val projectNewCardDesign: Boolean? = null,
    @SerialName("study_plan.track_feedback")
    val studyPlanTrackFeedback: Boolean? = null,
    @SerialName("discord_study_groups")
    val discordStudyGroups: Boolean? = null
)
