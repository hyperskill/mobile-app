package org.hyperskill.app.core.injection

import android.content.Context
import org.hyperskill.app.auth.injection.AuthCredentialsComponent
import org.hyperskill.app.auth.injection.AuthSocialComponent
import org.hyperskill.app.auth.injection.PlatformAuthCredentialsComponent
import org.hyperskill.app.auth.injection.PlatformAuthSocialComponent
import org.hyperskill.app.auth.injection.PlatformAuthSocialWebViewComponent
import org.hyperskill.app.debug.injection.DebugComponent
import org.hyperskill.app.debug.injection.PlatformDebugComponent
import org.hyperskill.app.home.injection.HomeComponent
import org.hyperskill.app.home.injection.PlatformHomeComponent
import org.hyperskill.app.main.injection.PlatformMainComponent
import org.hyperskill.app.notification_onboarding.injection.PlatformNotificationOnboardingComponent
import org.hyperskill.app.onboarding.injection.OnboardingComponent
import org.hyperskill.app.onboarding.injection.PlatformOnboardingComponent
import org.hyperskill.app.play_services.injection.PlayServicesCheckerComponent
import org.hyperskill.app.profile.injection.PlatformProfileComponent
import org.hyperskill.app.profile.injection.ProfileComponent
import org.hyperskill.app.profile_settings.injection.PlatformProfileSettingsComponent
import org.hyperskill.app.profile_settings.injection.ProfileSettingsComponent
import org.hyperskill.app.progress.injection.PlatformProgressScreenComponent
import org.hyperskill.app.project_selection.details.injection.PlatformProjectSelectionDetailsComponent
import org.hyperskill.app.project_selection.details.injection.ProjectSelectionDetailsParams
import org.hyperskill.app.project_selection.list.injection.PlatformProjectSelectionListComponent
import org.hyperskill.app.project_selection.list.injection.ProjectSelectionListParams
import org.hyperskill.app.stage_implementation.injection.PlatformStageImplementationComponent
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.injection.PlatformStepComponent
import org.hyperskill.app.step.injection.StepComponent
import org.hyperskill.app.step_quiz.injection.PlatformStepQuizComponent
import org.hyperskill.app.step_quiz.injection.StepQuizComponent
import org.hyperskill.app.step_quiz_hints.injection.PlatformStepQuizHintsComponent
import org.hyperskill.app.study_plan.injection.PlatformStudyPlanScreenComponent
import org.hyperskill.app.topics_repetitions.injection.PlatformTopicsRepetitionComponent
import org.hyperskill.app.track_selection.details.injection.PlatformTrackSelectionDetailsComponent
import org.hyperskill.app.track_selection.details.injection.TrackSelectionDetailsParams
import org.hyperskill.app.track_selection.list.injection.PlatformTrackSelectionListComponent
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListParams

interface CommonAndroidAppGraph : AppGraph {
    val context: Context

    val platformMainComponent: PlatformMainComponent

    fun buildPlatformAuthSocialWebViewComponent(): PlatformAuthSocialWebViewComponent

    fun buildPlatformAuthSocialComponent(authSocialComponent: AuthSocialComponent): PlatformAuthSocialComponent

    fun buildPlatformAuthCredentialsComponent(
        authCredentialsComponent: AuthCredentialsComponent
    ): PlatformAuthCredentialsComponent

    fun buildPlatformStepComponent(stepComponent: StepComponent): PlatformStepComponent

    fun buildPlatformStepQuizComponent(stepQuizComponent: StepQuizComponent): PlatformStepQuizComponent

    fun buildPlatformStepQuizHintsComponent(stepRoute: StepRoute, step: Step): PlatformStepQuizHintsComponent

    fun buildPlatformProfileComponent(profileComponent: ProfileComponent): PlatformProfileComponent

    fun buildPlatformProfileSettingsComponent(
        profileSettingsComponent: ProfileSettingsComponent
    ): PlatformProfileSettingsComponent

    fun buildPlatformHomeComponent(homeComponent: HomeComponent): PlatformHomeComponent

    fun buildPlatformOnboardingComponent(onboardingComponent: OnboardingComponent): PlatformOnboardingComponent

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

    fun buildPlatformNotificationOnboardingComponent(): PlatformNotificationOnboardingComponent
}