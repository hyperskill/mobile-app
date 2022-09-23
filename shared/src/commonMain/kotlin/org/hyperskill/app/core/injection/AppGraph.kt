package org.hyperskill.app.core.injection

import org.hyperskill.app.analytic.injection.AnalyticComponent
import org.hyperskill.app.auth.injection.AuthComponent
import org.hyperskill.app.auth.injection.AuthCredentialsComponent
import org.hyperskill.app.auth.injection.AuthSocialComponent
import org.hyperskill.app.home.injection.HomeComponent
import org.hyperskill.app.main.injection.MainComponent
import org.hyperskill.app.network.injection.NetworkComponent
import org.hyperskill.app.notification.injection.NotificationComponent
import org.hyperskill.app.onboarding.injection.OnboardingComponent
import org.hyperskill.app.placeholder_new_user.injection.PlaceholderNewUserComponent
import org.hyperskill.app.profile.injection.ProfileComponent
import org.hyperskill.app.profile.injection.ProfileDataComponent
import org.hyperskill.app.profile_settings.injection.ProfileSettingsComponent
import org.hyperskill.app.step.injection.StepComponent
import org.hyperskill.app.step_quiz.injection.StepQuizComponent
import org.hyperskill.app.step_quiz.injection.SubmissionDataComponent
import org.hyperskill.app.track.injection.TrackComponent

interface AppGraph {
    val commonComponent: CommonComponent
    val networkComponent: NetworkComponent
    val authComponent: AuthComponent
    val mainComponent: MainComponent
    val analyticComponent: AnalyticComponent
    val submissionDataComponent: SubmissionDataComponent

    fun buildAuthSocialComponent(): AuthSocialComponent
    fun buildAuthCredentialsComponent(): AuthCredentialsComponent
    fun buildStepComponent(): StepComponent
    fun buildStepQuizComponent(): StepQuizComponent
    fun buildProfileDataComponent(): ProfileDataComponent
    fun buildTrackComponent(): TrackComponent
    fun buildProfileComponent(): ProfileComponent
    fun buildProfileSettingsComponent(): ProfileSettingsComponent
    fun buildHomeComponent(): HomeComponent
    fun buildNotificationComponent(): NotificationComponent
    fun buildOnboardingComponent(): OnboardingComponent
    fun buildPlaceholderNewUserComponent(): PlaceholderNewUserComponent
}