package org.hyperskill.app.core.injection

import org.hyperskill.app.BuildConfig
import org.hyperskill.app.auth.injection.AuthCredentialsComponent
import org.hyperskill.app.auth.injection.AuthSocialComponent
import org.hyperskill.app.auth.injection.PlatformAuthCredentialsComponent
import org.hyperskill.app.auth.injection.PlatformAuthCredentialsComponentImpl
import org.hyperskill.app.auth.injection.PlatformAuthSocialComponent
import org.hyperskill.app.auth.injection.PlatformAuthSocialComponentImpl
import org.hyperskill.app.auth.injection.PlatformAuthSocialWebViewComponent
import org.hyperskill.app.auth.injection.PlatformAuthSocialWebViewComponentImpl
import org.hyperskill.app.debug.injection.DebugComponent
import org.hyperskill.app.debug.injection.PlatformDebugComponent
import org.hyperskill.app.debug.injection.PlatformDebugComponentImpl
import org.hyperskill.app.first_problem_onboarding.injection.PlatformFirstProblemOnboardingComponent
import org.hyperskill.app.first_problem_onboarding.injection.PlatformFirstProblemOnboardingComponentImpl
import org.hyperskill.app.home.injection.HomeComponent
import org.hyperskill.app.home.injection.PlatformHomeComponent
import org.hyperskill.app.home.injection.PlatformHomeComponentImpl
import org.hyperskill.app.interview_preparation_onboarding.injection.PlatformInterviewPreparationOnboardingComponent
import org.hyperskill.app.interview_preparation_onboarding.injection.PlatformInterviewPreparationOnboardingComponentImpl
import org.hyperskill.app.leaderboard.injection.PlatformLeaderboardComponent
import org.hyperskill.app.leaderboard.injection.PlatformLeaderboardComponentImpl
import org.hyperskill.app.notification.remote.injection.AndroidPlatformPushNotificationsPlatformDataComponent
import org.hyperskill.app.notification.remote.injection.PlatformPushNotificationsDataComponent
import org.hyperskill.app.notifications_onboarding.injection.PlatformNotificationsOnboardingComponent
import org.hyperskill.app.notifications_onboarding.injection.PlatformNotificationsOnboardingComponentImpl
import org.hyperskill.app.paywall.injection.PlatformPaywallComponent
import org.hyperskill.app.paywall.injection.PlatformPaywallComponentImpl
import org.hyperskill.app.play_services.injection.PlayServicesCheckerComponent
import org.hyperskill.app.play_services.injection.PlayServicesCheckerComponentImpl
import org.hyperskill.app.profile.injection.PlatformProfileComponent
import org.hyperskill.app.profile.injection.PlatformProfileComponentImpl
import org.hyperskill.app.profile.injection.ProfileComponent
import org.hyperskill.app.profile_settings.injection.PlatformProfileSettingsComponent
import org.hyperskill.app.profile_settings.injection.PlatformProfileSettingsComponentImpl
import org.hyperskill.app.profile_settings.injection.ProfileSettingsComponent
import org.hyperskill.app.progress.injection.PlatformProgressScreenComponent
import org.hyperskill.app.progress.injection.PlatformProgressScreenComponentImpl
import org.hyperskill.app.project_selection.details.injection.PlatformProjectSelectionDetailsComponent
import org.hyperskill.app.project_selection.details.injection.PlatformProjectSelectionDetailsComponentImpl
import org.hyperskill.app.project_selection.details.injection.ProjectSelectionDetailsParams
import org.hyperskill.app.project_selection.list.injection.PlatformProjectSelectionListComponent
import org.hyperskill.app.project_selection.list.injection.PlatformProjectSelectionListComponentImpl
import org.hyperskill.app.project_selection.list.injection.ProjectSelectionListParams
import org.hyperskill.app.purchases.domain.AndroidPurchaseManager
import org.hyperskill.app.purchases.injection.PurchaseComponent
import org.hyperskill.app.purchases.injection.PurchaseComponentImpl
import org.hyperskill.app.search.injection.PlatformSearchComponent
import org.hyperskill.app.search.injection.PlatformSearchComponentImpl
import org.hyperskill.app.stage_implementation.injection.PlatformStageImplementationComponent
import org.hyperskill.app.stage_implementation.injection.PlatformStageImplementationComponentImpl
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.injection.PlatformStepComponent
import org.hyperskill.app.step.injection.PlatformStepComponentImpl
import org.hyperskill.app.step.injection.StepComponent
import org.hyperskill.app.step_quiz.injection.PlatformStepQuizComponent
import org.hyperskill.app.step_quiz.injection.PlatformStepQuizComponentImpl
import org.hyperskill.app.step_quiz.injection.StepQuizComponent
import org.hyperskill.app.study_plan.injection.PlatformStudyPlanScreenComponent
import org.hyperskill.app.study_plan.injection.PlatformStudyPlanScreenComponentImpl
import org.hyperskill.app.topics_repetitions.injection.PlatformTopicsRepetitionComponent
import org.hyperskill.app.topics_repetitions.injection.PlatformTopicsRepetitionComponentImpl
import org.hyperskill.app.track_selection.details.injection.PlatformTrackSelectionDetailsComponent
import org.hyperskill.app.track_selection.details.injection.PlatformTrackSelectionDetailsComponentImpl
import org.hyperskill.app.track_selection.details.injection.TrackSelectionDetailsParams
import org.hyperskill.app.track_selection.list.injection.PlatformTrackSelectionListComponent
import org.hyperskill.app.track_selection.list.injection.PlatformTrackSelectionListComponentImpl
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListParams
import org.hyperskill.app.welcome.injection.PlatformWelcomeComponent
import org.hyperskill.app.welcome.injection.PlatformWelcomeComponentImpl
import org.hyperskill.app.welcome.injection.WelcomeComponent

abstract class CommonAndroidAppGraphImpl : CommonAndroidAppGraph, BaseAppGraph() {

    override fun buildPurchaseComponent(): PurchaseComponent =
        PurchaseComponentImpl(
            AndroidPurchaseManager(
                application = application,
                isDebugMode = BuildConfig.DEBUG
            )
        )

    override fun buildPlatformAuthSocialWebViewComponent(): PlatformAuthSocialWebViewComponent =
        PlatformAuthSocialWebViewComponentImpl(authSocialComponent = buildAuthSocialComponent())

    /**
     * Auth social component
     */
    override fun buildPlatformAuthSocialComponent(
        authSocialComponent: AuthSocialComponent
    ): PlatformAuthSocialComponent =
        PlatformAuthSocialComponentImpl(authSocialComponent)

    /**
     * Auth credentials component
     */
    override fun buildPlatformAuthCredentialsComponent(
        authCredentialsComponent: AuthCredentialsComponent
    ): PlatformAuthCredentialsComponent =
        PlatformAuthCredentialsComponentImpl(authCredentialsComponent)

    /**
     * Step component
     */
    override fun buildPlatformStepComponent(stepComponent: StepComponent): PlatformStepComponent =
        PlatformStepComponentImpl(stepComponent)

    /**
     * Step quiz component
     */
    override fun buildPlatformStepQuizComponent(stepQuizComponent: StepQuizComponent): PlatformStepQuizComponent =
        PlatformStepQuizComponentImpl(stepQuizComponent)

    /**
     * Stage implement component
     */
    override fun buildPlatformStageImplementationComponent(
        projectId: Long,
        stageId: Long
    ): PlatformStageImplementationComponent =
        PlatformStageImplementationComponentImpl(
            projectId = projectId,
            stageId = stageId,
            stageImplementationComponent = buildStageImplementComponent(projectId, stageId)
        )

    /**
     * Profile component
     */
    override fun buildPlatformProfileComponent(profileComponent: ProfileComponent): PlatformProfileComponent =
        PlatformProfileComponentImpl(profileComponent)

    /**
     * Profile settings component
     */
    override fun buildPlatformProfileSettingsComponent(
        profileSettingsComponent: ProfileSettingsComponent
    ): PlatformProfileSettingsComponent =
        PlatformProfileSettingsComponentImpl(profileSettingsComponent)

    /**
     * Home component
     */
    override fun buildPlatformHomeComponent(homeComponent: HomeComponent): PlatformHomeComponent =
        PlatformHomeComponentImpl(homeComponent)

    /**
     * Onboarding component
     */
    override fun buildPlatformWelcomeComponent(
        welcomeComponent: WelcomeComponent
    ): PlatformWelcomeComponent =
        PlatformWelcomeComponentImpl(welcomeComponent)

    /**
     * Topics repetitions component
     */
    override fun buildPlatformTopicsRepetitionsComponent(): PlatformTopicsRepetitionComponent =
        PlatformTopicsRepetitionComponentImpl(this)

    /**
     * Debug component
     */
    override fun buildPlatformDebugComponent(debugComponent: DebugComponent): PlatformDebugComponent =
        PlatformDebugComponentImpl(debugComponent)

    /**
     * Study plan component
     */
    override fun buildPlatformStudyPlanScreenComponent(): PlatformStudyPlanScreenComponent =
        PlatformStudyPlanScreenComponentImpl(
            studyPlanScreenComponent = buildStudyPlanScreenComponent()
        )

    /**
     * Project selection list component
     */
    override fun buildPlatformProjectSelectionListComponent(
        params: ProjectSelectionListParams
    ): PlatformProjectSelectionListComponent =
        PlatformProjectSelectionListComponentImpl(
            projectSelectionListComponent = buildProjectSelectionListComponent(),
            params = params
        )

    /**
     * Project selection details component
     */
    override fun buildPlatformProjectSelectionDetailsComponent(
        params: ProjectSelectionDetailsParams
    ): PlatformProjectSelectionDetailsComponent =
        PlatformProjectSelectionDetailsComponentImpl(buildProjectSelectionDetailsComponent(), params)

    /**
     * Track selection list component
     */
    override fun buildPlatformTrackSelectionListComponent(
        params: TrackSelectionListParams
    ): PlatformTrackSelectionListComponent =
        PlatformTrackSelectionListComponentImpl(buildTrackSelectionListComponent(), params)

    /**
     * Track selection details component
     */
    override fun buildPlatformTrackSelectionDetailsComponent(
        params: TrackSelectionDetailsParams
    ): PlatformTrackSelectionDetailsComponent =
        PlatformTrackSelectionDetailsComponentImpl(buildTrackSelectionDetailsComponent(), params)

    override fun buildPlatformProgressScreenComponent(): PlatformProgressScreenComponent =
        PlatformProgressScreenComponentImpl(buildProgressScreenComponent())

    override fun buildPlayServicesCheckerComponent(): PlayServicesCheckerComponent =
        PlayServicesCheckerComponentImpl(application, sentryComponent)

    override fun buildPlatformPushNotificationsDataComponent(): PlatformPushNotificationsDataComponent =
        AndroidPlatformPushNotificationsPlatformDataComponent(
            playServicesCheckerComponent = buildPlayServicesCheckerComponent(),
        )

    override fun buildPlatformNotificationOnboardingComponent(): PlatformNotificationsOnboardingComponent =
        PlatformNotificationsOnboardingComponentImpl(
            notificationsOnboardingComponent = buildNotificationsOnboardingComponent()
        )

    override fun buildPlatformFirstProblemOnboardingComponent(
        isNewUserMode: Boolean
    ): PlatformFirstProblemOnboardingComponent =
        PlatformFirstProblemOnboardingComponentImpl(
            isNewUserMode = isNewUserMode,
            firstProblemOnboardingComponent = buildFirstProblemOnboardingComponent()
        )

    override fun buildPlatformLeaderboardComponent(): PlatformLeaderboardComponent =
        PlatformLeaderboardComponentImpl(
            leaderboardComponent = buildLeaderboardScreenComponent()
        )

    override fun buildPlatformSearchComponent(): PlatformSearchComponent =
        PlatformSearchComponentImpl(
            searchComponent = buildSearchComponent()
        )

    override fun buildPlatformInterviewPreparationOnboardingComponent(
        stepRoute: StepRoute
    ): PlatformInterviewPreparationOnboardingComponent =
        PlatformInterviewPreparationOnboardingComponentImpl(
            interviewPreparationOnboardingComponent = buildInterviewPreparationOnboardingComponent(),
            stepRoute = stepRoute
        )

    override fun buildPlatformPaywallComponent(): PlatformPaywallComponent =
        PlatformPaywallComponentImpl(
            paywallComponent = buildPaywallComponent()
        )
}