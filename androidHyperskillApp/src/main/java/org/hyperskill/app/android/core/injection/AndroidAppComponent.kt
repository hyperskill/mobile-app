package org.hyperskill.app.android.core.injection

import android.content.Context
import org.hyperskill.app.android.code.injection.PlatformCodeEditorComponent
import org.hyperskill.app.android.image_loading.injection.ImageLoadingComponent
import org.hyperskill.app.android.latex.injection.PlatformLatexComponent
import org.hyperskill.app.android.main.injection.NavigationComponent
import org.hyperskill.app.android.notification.injection.PlatformNotificationComponent
import org.hyperskill.app.auth.injection.AuthCredentialsComponent
import org.hyperskill.app.auth.injection.AuthSocialComponent
import org.hyperskill.app.auth.injection.PlatformAuthCredentialsComponent
import org.hyperskill.app.auth.injection.PlatformAuthSocialComponent
import org.hyperskill.app.auth.injection.PlatformAuthSocialWebViewComponent
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.debug.injection.DebugComponent
import org.hyperskill.app.debug.injection.PlatformDebugComponent
import org.hyperskill.app.home.injection.HomeComponent
import org.hyperskill.app.home.injection.PlatformHomeComponent
import org.hyperskill.app.main.injection.PlatformMainComponent
import org.hyperskill.app.onboarding.injection.OnboardingComponent
import org.hyperskill.app.onboarding.injection.PlatformOnboardingComponent
import org.hyperskill.app.placeholder_new_user.injection.PlaceholderNewUserComponent
import org.hyperskill.app.placeholder_new_user.injection.PlatformPlaceholderNewUserComponent
import org.hyperskill.app.profile.injection.PlatformProfileComponent
import org.hyperskill.app.profile.injection.ProfileComponent
import org.hyperskill.app.profile_settings.injection.PlatformProfileSettingsComponent
import org.hyperskill.app.profile_settings.injection.ProfileSettingsComponent
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
import org.hyperskill.app.track.injection.PlatformTrackComponent
import org.hyperskill.app.track.injection.TrackComponent

interface AndroidAppComponent : AppGraph {
    val context: Context
    val platformMainComponent: PlatformMainComponent
    val platformNotificationComponent: PlatformNotificationComponent
    val imageLoadingComponent: ImageLoadingComponent
    val navigationComponent: NavigationComponent

    fun buildPlatformAuthSocialWebViewComponent(): PlatformAuthSocialWebViewComponent
    fun buildPlatformAuthSocialComponent(authSocialComponent: AuthSocialComponent): PlatformAuthSocialComponent
    fun buildPlatformAuthCredentialsComponent(authCredentialsComponent: AuthCredentialsComponent): PlatformAuthCredentialsComponent
    fun buildPlatformStepComponent(stepComponent: StepComponent): PlatformStepComponent
    fun buildPlatformStepQuizComponent(stepQuizComponent: StepQuizComponent): PlatformStepQuizComponent
    fun buildPlatformStepQuizHintsComponent(stepRoute: StepRoute, step: Step): PlatformStepQuizHintsComponent
    fun buildPlatformLatexComponent(): PlatformLatexComponent
    fun buildPlatformCodeEditorComponent(): PlatformCodeEditorComponent
    fun buildPlatformTrackComponent(trackComponent: TrackComponent): PlatformTrackComponent
    fun buildPlatformProfileComponent(profileComponent: ProfileComponent): PlatformProfileComponent
    fun buildPlatformProfileSettingsComponent(profileSettingsComponent: ProfileSettingsComponent): PlatformProfileSettingsComponent
    fun buildPlatformHomeComponent(homeComponent: HomeComponent): PlatformHomeComponent
    fun buildPlatformOnboardingComponent(onboardingComponent: OnboardingComponent): PlatformOnboardingComponent
    fun buildPlatformPlaceholderNewUserComponent(placeholderNewUserComponent: PlaceholderNewUserComponent): PlatformPlaceholderNewUserComponent
    fun buildPlatformTopicsRepetitionsComponent(): PlatformTopicsRepetitionComponent
    fun buildPlatformDebugComponent(debugComponent: DebugComponent): PlatformDebugComponent
    fun buildPlatformStageImplementationComponent(projectId: Long, stageId: Long): PlatformStageImplementationComponent
    fun buildPlatformStudyPlanScreenComponent(): PlatformStudyPlanScreenComponent
}