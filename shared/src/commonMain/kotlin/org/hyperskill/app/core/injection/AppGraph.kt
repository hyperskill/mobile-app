package org.hyperskill.app.core.injection

import org.hyperskill.app.analytic.injection.AnalyticComponent
import org.hyperskill.app.auth.injection.AuthComponent
import org.hyperskill.app.auth.injection.AuthCredentialsComponent
import org.hyperskill.app.auth.injection.AuthSocialComponent
import org.hyperskill.app.badges.injection.BadgesDataComponent
import org.hyperskill.app.comments.injection.CommentsDataComponent
import org.hyperskill.app.debug.injection.DebugComponent
import org.hyperskill.app.devices.injection.DevicesDataComponent
import org.hyperskill.app.discussions.injection.DiscussionsDataComponent
import org.hyperskill.app.freemium.injection.FreemiumDataComponent
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponent
import org.hyperskill.app.home.injection.HomeComponent
import org.hyperskill.app.items.injection.ItemsDataComponent
import org.hyperskill.app.learning_activities.injection.LearningActivitiesDataComponent
import org.hyperskill.app.likes.injection.LikesDataComponent
import org.hyperskill.app.magic_links.injection.MagicLinksDataComponent
import org.hyperskill.app.main.injection.MainComponent
import org.hyperskill.app.main.injection.MainDataComponent
import org.hyperskill.app.network.injection.NetworkComponent
import org.hyperskill.app.next_learning_activity_widget.injection.NextLearningActivityWidgetComponent
import org.hyperskill.app.notification.click_handling.injection.NotificationClickHandlingComponent
import org.hyperskill.app.notification.local.injection.NotificationComponent
import org.hyperskill.app.notification.local.injection.NotificationFlowDataComponent
import org.hyperskill.app.notification.remote.injection.PlatformPushNotificationsDataComponent
import org.hyperskill.app.notification.remote.injection.PushNotificationsComponent
import org.hyperskill.app.notifications_onboarding.injection.NotificationsOnboardingComponent
import org.hyperskill.app.onboarding.injection.OnboardingComponent
import org.hyperskill.app.problems_limit.domain.model.ProblemsLimitScreen
import org.hyperskill.app.problems_limit.injection.ProblemsLimitComponent
import org.hyperskill.app.products.injection.ProductsDataComponent
import org.hyperskill.app.profile.injection.ProfileComponent
import org.hyperskill.app.profile.injection.ProfileDataComponent
import org.hyperskill.app.profile_settings.injection.ProfileSettingsComponent
import org.hyperskill.app.progress_screen.injection.ProgressScreenComponent
import org.hyperskill.app.progresses.injection.ProgressesDataComponent
import org.hyperskill.app.progresses.injection.ProgressesFlowDataComponent
import org.hyperskill.app.project_selection.details.injection.ProjectSelectionDetailsComponent
import org.hyperskill.app.project_selection.list.injection.ProjectSelectionListComponent
import org.hyperskill.app.projects.injection.ProjectsDataComponent
import org.hyperskill.app.providers.injection.ProvidersDataComponent
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
import org.hyperskill.app.streak_recovery.injection.StreakRecoveryComponent
import org.hyperskill.app.streaks.injection.StreakFlowDataComponent
import org.hyperskill.app.streaks.injection.StreaksDataComponent
import org.hyperskill.app.study_plan.injection.StudyPlanDataComponent
import org.hyperskill.app.study_plan.screen.injection.StudyPlanScreenComponent
import org.hyperskill.app.study_plan.widget.injection.StudyPlanWidgetComponent
import org.hyperskill.app.topics.injection.TopicsDataComponent
import org.hyperskill.app.topics_repetitions.injection.TopicsRepetitionsComponent
import org.hyperskill.app.topics_repetitions.injection.TopicsRepetitionsDataComponent
import org.hyperskill.app.topics_repetitions.injection.TopicsRepetitionsFlowDataComponent
import org.hyperskill.app.track.injection.TrackDataComponent
import org.hyperskill.app.track_selection.details.injection.TrackSelectionDetailsComponent
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListComponent
import org.hyperskill.app.user_storage.injection.UserStorageComponent

interface AppGraph {
    val commonComponent: CommonComponent
    val networkComponent: NetworkComponent
    val authComponent: AuthComponent
    val mainComponent: MainComponent
    val analyticComponent: AnalyticComponent
    val sentryComponent: SentryComponent
    val submissionDataComponent: SubmissionDataComponent
    val streakFlowDataComponent: StreakFlowDataComponent
    val topicsRepetitionsFlowDataComponent: TopicsRepetitionsFlowDataComponent
    val stepCompletionFlowDataComponent: StepCompletionFlowDataComponent
    val progressesFlowDataComponent: ProgressesFlowDataComponent
    val notificationFlowDataComponent: NotificationFlowDataComponent
    val stateRepositoriesComponent: StateRepositoriesComponent
    val profileDataComponent: ProfileDataComponent

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
    fun buildStepQuizHintsComponent(stepRoute: StepRoute): StepQuizHintsComponent
    fun buildStepCompletionComponent(stepRoute: StepRoute): StepCompletionComponent
    fun buildStageImplementComponent(projectId: Long, stageId: Long): StageImplementComponent

    fun buildStudyPlanWidgetComponent(): StudyPlanWidgetComponent

    fun buildStudyPlanScreenComponent(): StudyPlanScreenComponent

    fun buildMainDataComponent(): MainDataComponent
    fun buildTrackDataComponent(): TrackDataComponent
    fun buildTrackSelectionListComponent(): TrackSelectionListComponent
    fun buildTrackSelectionDetailsComponent(): TrackSelectionDetailsComponent
    fun buildProfileComponent(): ProfileComponent
    fun buildProfileSettingsComponent(): ProfileSettingsComponent
    fun buildHomeComponent(): HomeComponent
    fun buildNotificationComponent(): NotificationComponent
    fun buildOnboardingComponent(): OnboardingComponent
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
    fun buildGamificationToolbarComponent(screen: GamificationToolbarScreen): GamificationToolbarComponent
    fun buildStudyPlanDataComponent(): StudyPlanDataComponent
    fun buildProjectsDataComponent(): ProjectsDataComponent
    fun buildProjectSelectionListComponent(): ProjectSelectionListComponent
    fun buildProjectSelectionDetailsComponent(): ProjectSelectionDetailsComponent
    fun buildStagesDataComponent(): StagesDataComponent
    fun buildFreemiumDataComponent(): FreemiumDataComponent
    fun buildProblemsLimitComponent(screen: ProblemsLimitScreen): ProblemsLimitComponent
    fun buildProvidersDataComponent(): ProvidersDataComponent
    fun buildStreakRecoveryComponent(): StreakRecoveryComponent
    fun buildDevicesDataComponent(): DevicesDataComponent
    fun buildPushNotificationsComponent(): PushNotificationsComponent
    fun buildPlatformPushNotificationsDataComponent(): PlatformPushNotificationsDataComponent
    fun buildClickedNotificationComponent(): NotificationClickHandlingComponent
    fun buildProgressScreenComponent(): ProgressScreenComponent
    fun buildNextLearningActivityWidgetComponent(): NextLearningActivityWidgetComponent
    fun buildBadgesDataComponent(): BadgesDataComponent
    fun buildNotificationsOnboardingComponent(): NotificationsOnboardingComponent
}