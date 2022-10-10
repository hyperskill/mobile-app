package org.hyperskill.app.core.injection

import org.hyperskill.app.analytic.injection.AnalyticComponent
import org.hyperskill.app.analytic.injection.AnalyticComponentImpl
import org.hyperskill.app.auth.injection.AuthComponent
import org.hyperskill.app.auth.injection.AuthComponentImpl
import org.hyperskill.app.auth.injection.AuthCredentialsComponent
import org.hyperskill.app.auth.injection.AuthCredentialsComponentImpl
import org.hyperskill.app.auth.injection.AuthSocialComponent
import org.hyperskill.app.auth.injection.AuthSocialComponentImpl
import org.hyperskill.app.comments.injection.CommentsDataComponent
import org.hyperskill.app.comments.injection.CommentsDataComponentImpl
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.home.injection.HomeComponent
import org.hyperskill.app.home.injection.HomeComponentImpl
import org.hyperskill.app.main.injection.MainComponent
import org.hyperskill.app.main.injection.MainComponentImpl
import org.hyperskill.app.network.injection.NetworkComponent
import org.hyperskill.app.network.injection.NetworkComponentImpl
import org.hyperskill.app.notification.injection.NotificationComponent
import org.hyperskill.app.notification.injection.NotificationComponentImpl
import org.hyperskill.app.onboarding.injection.OnboardingComponent
import org.hyperskill.app.onboarding.injection.OnboardingComponentImpl
import org.hyperskill.app.placeholder_new_user.injection.PlaceholderNewUserComponent
import org.hyperskill.app.placeholder_new_user.injection.PlaceholderNewUserComponentImpl
import org.hyperskill.app.profile.injection.ProfileComponent
import org.hyperskill.app.profile.injection.ProfileComponentImpl
import org.hyperskill.app.profile.injection.ProfileDataComponent
import org.hyperskill.app.profile.injection.ProfileDataComponentImpl
import org.hyperskill.app.profile_settings.injection.ProfileSettingsComponent
import org.hyperskill.app.profile_settings.injection.ProfileSettingsComponentImpl
import org.hyperskill.app.step.injection.StepComponent
import org.hyperskill.app.step.injection.StepComponentImpl
import org.hyperskill.app.step_quiz.injection.StepQuizComponent
import org.hyperskill.app.step_quiz.injection.StepQuizComponentImpl
import org.hyperskill.app.step_quiz.injection.SubmissionDataComponent
import org.hyperskill.app.step_quiz.injection.SubmissionDataComponentImpl
import org.hyperskill.app.track.injection.TrackComponent
import org.hyperskill.app.track.injection.TrackComponentImpl
import org.hyperskill.app.user_storage.injection.UserStorageComponent
import org.hyperskill.app.user_storage.injection.UserStorageComponentImpl

class AppGraphImpl(
    userAgentInfo: UserAgentInfo,
    buildVariant: BuildVariant
) : iOSAppComponent {
    override val commonComponent: CommonComponent =
        CommonComponentImpl(userAgentInfo, buildVariant)

    override val networkComponent: NetworkComponent =
        NetworkComponentImpl(this)

    override val submissionDataComponent: SubmissionDataComponent =
        SubmissionDataComponentImpl(this)

    override val authComponent: AuthComponent =
        AuthComponentImpl(this)

    override val analyticComponent: AnalyticComponent =
        AnalyticComponentImpl(this)

    override val mainComponent: MainComponent =
        MainComponentImpl(this)

    override fun buildAuthSocialComponent(): AuthSocialComponent =
        AuthSocialComponentImpl(
            commonComponent,
            authComponent,
            buildProfileDataComponent(),
            analyticComponent
        )

    override fun buildAuthCredentialsComponent(): AuthCredentialsComponent =
        AuthCredentialsComponentImpl(
            commonComponent,
            authComponent,
            buildProfileDataComponent(),
            analyticComponent
        )

    override fun buildStepComponent(): StepComponent =
        StepComponentImpl(this)

    override fun buildStepQuizComponent(): StepQuizComponent =
        StepQuizComponentImpl(this)

    override fun buildProfileDataComponent(): ProfileDataComponent =
        ProfileDataComponentImpl(this)

    override fun buildTrackComponent(): TrackComponent =
        TrackComponentImpl(this)

    override fun buildProfileComponent(): ProfileComponent =
        ProfileComponentImpl(this)

    override fun buildProfileSettingsComponent(): ProfileSettingsComponent =
        ProfileSettingsComponentImpl(this)

    override fun buildHomeComponent(): HomeComponent =
        HomeComponentImpl(this)

    override fun buildNotificationComponent(): NotificationComponent =
        NotificationComponentImpl(this)

    override fun buildOnboardingComponent(): OnboardingComponent =
        OnboardingComponentImpl(this)

    override fun buildPlaceholderNewUserComponent(): PlaceholderNewUserComponent =
        PlaceholderNewUserComponentImpl(this)

    override fun buildUserStorageComponent(): UserStorageComponent =
        UserStorageComponentImpl(this)

    override fun buildCommentsDataComponent(): CommentsDataComponent =
        CommentsDataComponentImpl(this)
}