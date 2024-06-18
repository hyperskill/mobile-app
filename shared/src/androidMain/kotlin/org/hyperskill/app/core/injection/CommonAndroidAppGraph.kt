package org.hyperskill.app.core.injection

import android.app.Application
import org.hyperskill.app.analytic.injection.PlatformAnalyticComponent
import org.hyperskill.app.auth.injection.AuthCredentialsComponent
import org.hyperskill.app.auth.injection.AuthSocialComponent
import org.hyperskill.app.auth.injection.PlatformAuthCredentialsComponent
import org.hyperskill.app.auth.injection.PlatformAuthSocialComponent
import org.hyperskill.app.auth.injection.PlatformAuthSocialWebViewComponent
import org.hyperskill.app.debug.injection.DebugComponent
import org.hyperskill.app.debug.injection.PlatformDebugComponent
import org.hyperskill.app.first_problem_onboarding.injection.PlatformFirstProblemOnboardingComponent
import org.hyperskill.app.home.injection.HomeComponent
import org.hyperskill.app.home.injection.PlatformHomeComponent
import org.hyperskill.app.leaderboard.injection.PlatformLeaderboardComponent
import org.hyperskill.app.main.injection.PlatformMainComponent
import org.hyperskill.app.manage_subscription.injection.PlatformManageSubscriptionComponent
import org.hyperskill.app.notifications_onboarding.injection.PlatformNotificationsOnboardingComponent
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.paywall.injection.PlatformPaywallComponent
import org.hyperskill.app.play_services.injection.PlayServicesCheckerComponent
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalFeatureParams
import org.hyperskill.app.problems_limit_info.injection.PlatformProblemsLimitInfoModalComponent
import org.hyperskill.app.profile.injection.PlatformProfileComponent
import org.hyperskill.app.profile.injection.ProfileComponent
import org.hyperskill.app.profile_settings.injection.PlatformProfileSettingsComponent
import org.hyperskill.app.profile_settings.injection.ProfileSettingsComponent
import org.hyperskill.app.progress.injection.PlatformProgressScreenComponent
import org.hyperskill.app.project_selection.details.injection.PlatformProjectSelectionDetailsComponent
import org.hyperskill.app.project_selection.details.injection.ProjectSelectionDetailsParams
import org.hyperskill.app.project_selection.list.injection.PlatformProjectSelectionListComponent
import org.hyperskill.app.project_selection.list.injection.ProjectSelectionListParams
import org.hyperskill.app.request_review.injection.PlatformRequestReviewComponent
import org.hyperskill.app.search.injection.PlatformSearchComponent
import org.hyperskill.app.stage_implementation.injection.PlatformStageImplementationComponent
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.injection.PlatformStepComponent
import org.hyperskill.app.step_feedback.injection.PlatformStepFeedbackComponent
import org.hyperskill.app.step_quiz.injection.PlatformStepQuizComponent
import org.hyperskill.app.step_quiz.injection.StepQuizComponent
import org.hyperskill.app.study_plan.injection.PlatformStudyPlanScreenComponent
import org.hyperskill.app.topic_completed_modal.domain.model.TopicCompletedModalFeatureParams
import org.hyperskill.app.topic_completed_modal.injection.PlatformTopicCompletedModalComponent
import org.hyperskill.app.topics_repetitions.injection.PlatformTopicsRepetitionComponent
import org.hyperskill.app.track_selection.details.injection.PlatformTrackSelectionDetailsComponent
import org.hyperskill.app.track_selection.details.injection.TrackSelectionDetailsParams
import org.hyperskill.app.track_selection.list.injection.PlatformTrackSelectionListComponent
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListParams
import org.hyperskill.app.welcome.injection.PlatformWelcomeComponent
import org.hyperskill.app.welcome.injection.WelcomeComponent
import org.hyperskill.app.welcome_onboarding.root.injection.PlatformWelcomeOnboardingComponent
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingFeatureParams
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.track_details.injection.PlatformWelcomeOnboardingTrackDetailsComponent

interface CommonAndroidAppGraph : AppGraph {
    val application: Application

    val platformMainComponent: PlatformMainComponent

    val platformAnalyticComponent: PlatformAnalyticComponent

    fun buildPlatformAuthSocialWebViewComponent(): PlatformAuthSocialWebViewComponent

    fun buildPlatformAuthSocialComponent(authSocialComponent: AuthSocialComponent): PlatformAuthSocialComponent

    fun buildPlatformAuthCredentialsComponent(
        authCredentialsComponent: AuthCredentialsComponent
    ): PlatformAuthCredentialsComponent

    fun buildPlatformStepComponent(stepRoute: StepRoute): PlatformStepComponent

    fun buildPlatformStepQuizComponent(stepQuizComponent: StepQuizComponent): PlatformStepQuizComponent

    fun buildPlatformStepFeedbackComponent(stepRoute: StepRoute): PlatformStepFeedbackComponent

    fun buildPlatformProfileComponent(profileComponent: ProfileComponent): PlatformProfileComponent

    fun buildPlatformProfileSettingsComponent(
        profileSettingsComponent: ProfileSettingsComponent
    ): PlatformProfileSettingsComponent

    fun buildPlatformHomeComponent(homeComponent: HomeComponent): PlatformHomeComponent

    fun buildPlatformWelcomeComponent(welcomeComponent: WelcomeComponent): PlatformWelcomeComponent

    fun buildPlatformTopicsRepetitionsComponent(): PlatformTopicsRepetitionComponent

    fun buildPlatformDebugComponent(debugComponent: DebugComponent): PlatformDebugComponent

    fun buildPlatformStageImplementationComponent(projectId: Long, stageId: Long): PlatformStageImplementationComponent

    fun buildPlatformStudyPlanScreenComponent(): PlatformStudyPlanScreenComponent

    fun buildPlatformProjectSelectionListComponent(
        params: ProjectSelectionListParams
    ): PlatformProjectSelectionListComponent

    fun buildPlatformProjectSelectionDetailsComponent(
        params: ProjectSelectionDetailsParams
    ): PlatformProjectSelectionDetailsComponent

    fun buildPlatformTrackSelectionListComponent(params: TrackSelectionListParams): PlatformTrackSelectionListComponent

    fun buildPlatformTrackSelectionDetailsComponent(
        params: TrackSelectionDetailsParams
    ): PlatformTrackSelectionDetailsComponent

    fun buildPlatformProgressScreenComponent(): PlatformProgressScreenComponent

    fun buildPlayServicesCheckerComponent(): PlayServicesCheckerComponent

    fun buildPlatformNotificationOnboardingComponent(): PlatformNotificationsOnboardingComponent

    fun buildPlatformFirstProblemOnboardingComponent(isNewUserMode: Boolean): PlatformFirstProblemOnboardingComponent

    fun buildPlatformLeaderboardComponent(): PlatformLeaderboardComponent

    fun buildPlatformSearchComponent(): PlatformSearchComponent

    fun buildPlatformRequestReviewComponent(
        stepRoute: StepRoute
    ): PlatformRequestReviewComponent

    fun buildPlatformPaywallComponent(paywallTransitionSource: PaywallTransitionSource): PlatformPaywallComponent

    fun buildPlatformManageSubscriptionComponent(): PlatformManageSubscriptionComponent

    fun buildPlatformProblemsLimitInfoModalComponent(
        params: ProblemsLimitInfoModalFeatureParams
    ): PlatformProblemsLimitInfoModalComponent

    fun buildPlatformTopicCompletedModalComponent(
        params: TopicCompletedModalFeatureParams
    ): PlatformTopicCompletedModalComponent

    fun buildPlatformWelcomeOnboardingComponent(
        params: WelcomeOnboardingFeatureParams
    ): PlatformWelcomeOnboardingComponent

    fun buildPlatformWelcomeOnboardingTrackDetailsComponent(
        track: WelcomeOnboardingTrack
    ): PlatformWelcomeOnboardingTrackDetailsComponent
}