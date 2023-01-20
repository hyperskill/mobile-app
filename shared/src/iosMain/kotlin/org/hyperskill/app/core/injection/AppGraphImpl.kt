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
import org.hyperskill.app.debug.injection.DebugComponent
import org.hyperskill.app.debug.injection.DebugComponentImpl
import org.hyperskill.app.discussions.injection.DiscussionsDataComponent
import org.hyperskill.app.discussions.injection.DiscussionsDataComponentImpl
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponent
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponentImpl
import org.hyperskill.app.home.injection.HomeComponent
import org.hyperskill.app.home.injection.HomeComponentImpl
import org.hyperskill.app.items.injection.ItemsDataComponent
import org.hyperskill.app.items.injection.ItemsDataComponentImpl
import org.hyperskill.app.learning_activities.injection.LearningActivitiesDataComponent
import org.hyperskill.app.learning_activities.injection.LearningActivitiesDataComponentImpl
import org.hyperskill.app.likes.injection.LikesDataComponent
import org.hyperskill.app.likes.injection.LikesDataComponentImpl
import org.hyperskill.app.magic_links.injection.MagicLinksDataComponent
import org.hyperskill.app.magic_links.injection.MagicLinksDataComponentImpl
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
import org.hyperskill.app.products.injection.ProductsDataComponent
import org.hyperskill.app.products.injection.ProductsDataComponentImpl
import org.hyperskill.app.profile.injection.ProfileComponent
import org.hyperskill.app.profile.injection.ProfileComponentImpl
import org.hyperskill.app.profile.injection.ProfileDataComponent
import org.hyperskill.app.profile.injection.ProfileDataComponentImpl
import org.hyperskill.app.profile.injection.ProfileHypercoinsDataComponent
import org.hyperskill.app.profile.injection.ProfileHypercoinsDataComponentImpl
import org.hyperskill.app.profile_settings.injection.ProfileSettingsComponent
import org.hyperskill.app.profile_settings.injection.ProfileSettingsComponentImpl
import org.hyperskill.app.progresses.injection.ProgressesDataComponent
import org.hyperskill.app.progresses.injection.ProgressesDataComponentImpl
import org.hyperskill.app.reactions.injection.ReactionsDataComponent
import org.hyperskill.app.reactions.injection.ReactionsDataComponentImpl
import org.hyperskill.app.sentry.domain.model.manager.SentryManager
import org.hyperskill.app.sentry.injection.SentryComponent
import org.hyperskill.app.sentry.injection.SentryComponentImpl
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.injection.StepComponent
import org.hyperskill.app.step.injection.StepComponentImpl
import org.hyperskill.app.step_quiz.injection.StepQuizComponent
import org.hyperskill.app.step_quiz.injection.StepQuizComponentImpl
import org.hyperskill.app.step_quiz.injection.SubmissionDataComponent
import org.hyperskill.app.step_quiz.injection.SubmissionDataComponentImpl
import org.hyperskill.app.step_quiz_hints.injection.StepQuizHintsComponent
import org.hyperskill.app.step_quiz_hints.injection.StepQuizHintsComponentImpl
import org.hyperskill.app.streaks.injection.StreaksDataComponent
import org.hyperskill.app.streaks.injection.StreaksDataComponentImpl
import org.hyperskill.app.topics.injection.TopicsDataComponent
import org.hyperskill.app.topics.injection.TopicsDataComponentImpl
import org.hyperskill.app.topics_repetitions.injection.TopicsRepetitionsComponent
import org.hyperskill.app.topics_repetitions.injection.TopicsRepetitionsComponentImpl
import org.hyperskill.app.topics_repetitions.injection.TopicsRepetitionsDataComponent
import org.hyperskill.app.topics_repetitions.injection.TopicsRepetitionsDataComponentImpl
import org.hyperskill.app.track.injection.TrackComponent
import org.hyperskill.app.track.injection.TrackComponentImpl
import org.hyperskill.app.track.injection.TrackDataComponent
import org.hyperskill.app.track.injection.TrackDataComponentImpl
import org.hyperskill.app.user_storage.injection.UserStorageComponent
import org.hyperskill.app.user_storage.injection.UserStorageComponentImpl

class AppGraphImpl(
    userAgentInfo: UserAgentInfo,
    buildVariant: BuildVariant,
    sentryManager: SentryManager
) : iOSAppComponent {
    override val commonComponent: CommonComponent =
        CommonComponentImpl(buildVariant, userAgentInfo)

    override val networkComponent: NetworkComponent =
        NetworkComponentImpl(this)

    override val submissionDataComponent: SubmissionDataComponent =
        SubmissionDataComponentImpl(this)

    override val authComponent: AuthComponent =
        AuthComponentImpl(this)

    override val profileHypercoinsDataComponent: ProfileHypercoinsDataComponent =
        ProfileHypercoinsDataComponentImpl()

    override val analyticComponent: AnalyticComponent =
        AnalyticComponentImpl(this)

    override val sentryComponent: SentryComponent =
        SentryComponentImpl(sentryManager)

    override val mainComponent: MainComponent =
        MainComponentImpl(this)

    override val topicsRepetitionsDataComponent: TopicsRepetitionsDataComponent =
        TopicsRepetitionsDataComponentImpl(this)

    override fun buildAuthSocialComponent(): AuthSocialComponent =
        AuthSocialComponentImpl(
            commonComponent,
            authComponent,
            buildProfileDataComponent(),
            analyticComponent,
            sentryComponent
        )

    override fun buildAuthCredentialsComponent(): AuthCredentialsComponent =
        AuthCredentialsComponentImpl(
            commonComponent,
            authComponent,
            buildProfileDataComponent(),
            buildMagicLinksDataComponent(),
            analyticComponent,
            sentryComponent
        )

    override fun buildStepComponent(): StepComponent =
        StepComponentImpl(this)

    override fun buildStepQuizComponent(stepRoute: StepRoute): StepQuizComponent =
        StepQuizComponentImpl(this, stepRoute)

    override fun buildProfileDataComponent(): ProfileDataComponent =
        ProfileDataComponentImpl(this)

    override fun buildTrackComponent(): TrackComponent =
        TrackComponentImpl(this)

    override fun buildTrackDataComponent(): TrackDataComponent =
        TrackDataComponentImpl(this)

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

    override fun buildStepQuizHintsComponent(): StepQuizHintsComponent =
        StepQuizHintsComponentImpl(this)

    override fun buildMagicLinksDataComponent(): MagicLinksDataComponent =
        MagicLinksDataComponentImpl(this)

    override fun buildDiscussionsDataComponent(): DiscussionsDataComponent =
        DiscussionsDataComponentImpl(this)

    override fun buildReactionsDataComponent(): ReactionsDataComponent =
        ReactionsDataComponentImpl(this)

    override fun buildLikesDataComponent(): LikesDataComponent =
        LikesDataComponentImpl(this)

    override fun buildTopicsRepetitionsComponent(): TopicsRepetitionsComponent =
        TopicsRepetitionsComponentImpl(this)

    override fun buildLearningActivitiesDataComponent(): LearningActivitiesDataComponent =
        LearningActivitiesDataComponentImpl(this)

    override fun buildTopicsDataComponent(): TopicsDataComponent =
        TopicsDataComponentImpl(this)

    override fun buildProgressesDataComponent(): ProgressesDataComponent =
        ProgressesDataComponentImpl(this)

    override fun buildProductsDataComponent(): ProductsDataComponent =
        ProductsDataComponentImpl(this)

    override fun buildItemsDataComponent(): ItemsDataComponent =
        ItemsDataComponentImpl(this)

    override fun buildStreaksDataComponent(): StreaksDataComponent =
        StreaksDataComponentImpl(this)

    override fun buildDebugComponent(): DebugComponent =
        DebugComponentImpl(this)

    override fun buildGamificationToolbarComponent(): GamificationToolbarComponent =
        GamificationToolbarComponentImpl(this)
}