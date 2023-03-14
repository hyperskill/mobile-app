package org.hyperskill.app.core.injection

import org.hyperskill.app.analytic.injection.AnalyticComponent
import org.hyperskill.app.auth.injection.AuthComponent
import org.hyperskill.app.auth.injection.AuthCredentialsComponent
import org.hyperskill.app.auth.injection.AuthSocialComponent
import org.hyperskill.app.comments.injection.CommentsDataComponent
import org.hyperskill.app.debug.injection.DebugComponent
import org.hyperskill.app.discussions.injection.DiscussionsDataComponent
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponent
import org.hyperskill.app.home.injection.HomeComponent
import org.hyperskill.app.items.injection.ItemsDataComponent
import org.hyperskill.app.learning_activities.injection.LearningActivitiesDataComponent
import org.hyperskill.app.likes.injection.LikesDataComponent
import org.hyperskill.app.magic_links.injection.MagicLinksDataComponent
import org.hyperskill.app.main.injection.MainComponent
import org.hyperskill.app.main.injection.MainDataComponent
import org.hyperskill.app.network.injection.NetworkComponent
import org.hyperskill.app.notification.injection.NotificationComponent
import org.hyperskill.app.notification.injection.NotificationFlowDataComponent
import org.hyperskill.app.onboarding.injection.OnboardingComponent
import org.hyperskill.app.placeholder_new_user.injection.PlaceholderNewUserComponent
import org.hyperskill.app.products.injection.ProductsDataComponent
import org.hyperskill.app.profile.injection.ProfileComponent
import org.hyperskill.app.profile.injection.ProfileDataComponent
import org.hyperskill.app.profile.injection.ProfileHypercoinsDataComponent
import org.hyperskill.app.profile_settings.injection.ProfileSettingsComponent
import org.hyperskill.app.progresses.injection.ProgressesDataComponent
import org.hyperskill.app.progresses.injection.ProgressesFlowDataComponent
import org.hyperskill.app.projects.injection.ProjectsDataComponent
import org.hyperskill.app.reactions.injection.ReactionsDataComponent
import org.hyperskill.app.sentry.injection.SentryComponent
import org.hyperskill.app.stage_implement.injection.StageImplementComponent
import org.hyperskill.app.stages.injection.StagesDataComponent
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.injection.StepComponent
import org.hyperskill.app.step.injection.StepDataComponent
import org.hyperskill.app.step_completion.injection.StepCompletionComponent
import org.hyperskill.app.step_completion.injection.StepCompletionFlowDataComponent
import org.hyperskill.app.step_quiz.injection.StepQuizComponent
import org.hyperskill.app.step_quiz.injection.SubmissionDataComponent
import org.hyperskill.app.step_quiz_hints.injection.StepQuizHintsComponent
import org.hyperskill.app.streaks.injection.StreakFlowDataComponent
import org.hyperskill.app.streaks.injection.StreaksDataComponent
import org.hyperskill.app.study_plan.injection.StudyPlanDataComponent
import org.hyperskill.app.topics.injection.TopicsDataComponent
import org.hyperskill.app.topics_repetitions.injection.TopicsRepetitionsComponent
import org.hyperskill.app.topics_repetitions.injection.TopicsRepetitionsDataComponent
import org.hyperskill.app.topics_repetitions.injection.TopicsRepetitionsFlowDataComponent
import org.hyperskill.app.topics_to_discover_next.domain.model.TopicsToDiscoverNextScreen
import org.hyperskill.app.topics_to_discover_next.injection.TopicsToDiscoverNextComponent
import org.hyperskill.app.topics_to_discover_next.injection.TopicsToDiscoverNextDataComponent
import org.hyperskill.app.track.injection.TrackComponent
import org.hyperskill.app.track.injection.TrackDataComponent
import org.hyperskill.app.user_storage.injection.UserStorageComponent

interface AppGraph {
    val commonComponent: CommonComponent
    val networkComponent: NetworkComponent
    val authComponent: AuthComponent
    val mainComponent: MainComponent
    val analyticComponent: AnalyticComponent
    val sentryComponent: SentryComponent
    val submissionDataComponent: SubmissionDataComponent
    val profileHypercoinsDataComponent: ProfileHypercoinsDataComponent
    val streakFlowDataComponent: StreakFlowDataComponent
    val topicsRepetitionsFlowDataComponent: TopicsRepetitionsFlowDataComponent
    val stepCompletionFlowDataComponent: StepCompletionFlowDataComponent
    val progressesFlowDataComponent: ProgressesFlowDataComponent
    val notificationFlowDataComponent: NotificationFlowDataComponent

    /**
     * Auth components
     */
    fun buildAuthSocialComponent(): AuthSocialComponent
    fun buildAuthCredentialsComponent(): AuthCredentialsComponent

    /**
     * Step components
     */
    fun buildStepComponent(stepRoute: StepRoute): StepComponent
    fun buildStepDataComponent(): StepDataComponent
    fun buildStepQuizComponent(stepRoute: StepRoute): StepQuizComponent
    fun buildStepQuizHintsComponent(): StepQuizHintsComponent
    fun buildStepCompletionComponent(stepRoute: StepRoute): StepCompletionComponent
    fun buildStageImplementComponent(projectId: Long, stageId: Long): StageImplementComponent

    fun buildMainDataComponent(): MainDataComponent
    fun buildProfileDataComponent(): ProfileDataComponent
    fun buildTrackComponent(): TrackComponent
    fun buildTrackDataComponent(): TrackDataComponent
    fun buildProfileComponent(): ProfileComponent
    fun buildProfileSettingsComponent(): ProfileSettingsComponent
    fun buildHomeComponent(): HomeComponent
    fun buildNotificationComponent(): NotificationComponent
    fun buildOnboardingComponent(): OnboardingComponent
    fun buildPlaceholderNewUserComponent(): PlaceholderNewUserComponent
    fun buildUserStorageComponent(): UserStorageComponent
    fun buildCommentsDataComponent(): CommentsDataComponent
    fun buildStreaksDataComponent(): StreaksDataComponent
    fun buildMagicLinksDataComponent(): MagicLinksDataComponent
    fun buildDiscussionsDataComponent(): DiscussionsDataComponent
    fun buildReactionsDataComponent(): ReactionsDataComponent
    fun buildLikesDataComponent(): LikesDataComponent
    fun buildTopicsRepetitionsComponent(): TopicsRepetitionsComponent
    fun buildTopicsRepetitionsDataComponent(): TopicsRepetitionsDataComponent
    fun buildLearningActivitiesDataComponent(): LearningActivitiesDataComponent
    fun buildTopicsDataComponent(): TopicsDataComponent
    fun buildProgressesDataComponent(): ProgressesDataComponent
    fun buildProductsDataComponent(): ProductsDataComponent
    fun buildItemsDataComponent(): ItemsDataComponent
    fun buildDebugComponent(): DebugComponent
    fun buildGamificationToolbarComponent(): GamificationToolbarComponent
    fun buildTopicsToDiscoverNextComponent(screen: TopicsToDiscoverNextScreen): TopicsToDiscoverNextComponent
    fun buildTopicsToDiscoverNextDataComponent(): TopicsToDiscoverNextDataComponent
    fun buildStudyPlanDataComponent(): StudyPlanDataComponent
    fun buildProjectsDataComponent(): ProjectsDataComponent
    fun buildStagesDataComponent(): StagesDataComponent
}