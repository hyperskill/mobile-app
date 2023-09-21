package org.hyperskill.app.core.injection

import org.hyperskill.app.analytic.injection.AnalyticComponent
import org.hyperskill.app.analytic.injection.AnalyticComponentImpl
import org.hyperskill.app.auth.injection.AuthComponent
import org.hyperskill.app.auth.injection.AuthComponentImpl
import org.hyperskill.app.auth.injection.AuthCredentialsComponent
import org.hyperskill.app.auth.injection.AuthCredentialsComponentImpl
import org.hyperskill.app.auth.injection.AuthSocialComponent
import org.hyperskill.app.auth.injection.AuthSocialComponentImpl
import org.hyperskill.app.badges.injection.BadgesDataComponent
import org.hyperskill.app.badges.injection.BadgesDataComponentImpl
import org.hyperskill.app.comments.injection.CommentsDataComponent
import org.hyperskill.app.comments.injection.CommentsDataComponentImpl
import org.hyperskill.app.debug.injection.DebugComponent
import org.hyperskill.app.debug.injection.DebugComponentImpl
import org.hyperskill.app.devices.injection.DevicesDataComponent
import org.hyperskill.app.devices.injection.DevicesDataComponentImpl
import org.hyperskill.app.discussions.injection.DiscussionsDataComponent
import org.hyperskill.app.discussions.injection.DiscussionsDataComponentImpl
import org.hyperskill.app.freemium.injection.FreemiumDataComponent
import org.hyperskill.app.freemium.injection.FreemiumDataComponentImpl
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
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
import org.hyperskill.app.main.injection.MainDataComponent
import org.hyperskill.app.main.injection.MainDataComponentImpl
import org.hyperskill.app.network.injection.NetworkComponent
import org.hyperskill.app.network.injection.NetworkComponentImpl
import org.hyperskill.app.next_learning_activity_widget.injection.NextLearningActivityWidgetComponent
import org.hyperskill.app.next_learning_activity_widget.injection.NextLearningActivityWidgetComponentImpl
import org.hyperskill.app.notification.click_handling.injection.NotificationClickHandlingComponent
import org.hyperskill.app.notification.click_handling.injection.NotificationClickHandlingComponentImpl
import org.hyperskill.app.notification.local.injection.NotificationComponent
import org.hyperskill.app.notification.local.injection.NotificationComponentImpl
import org.hyperskill.app.notification.local.injection.NotificationFlowDataComponent
import org.hyperskill.app.notification.local.injection.NotificationFlowDataComponentImpl
import org.hyperskill.app.notification.remote.injection.PushNotificationsComponent
import org.hyperskill.app.notification.remote.injection.PushNotificationsComponentImpl
import org.hyperskill.app.notifications_onboarding.injection.NotificationsOnboardingComponent
import org.hyperskill.app.notifications_onboarding.injection.NotificationsOnboardingComponentImpl
import org.hyperskill.app.onboarding.injection.OnboardingComponent
import org.hyperskill.app.onboarding.injection.OnboardingComponentImpl
import org.hyperskill.app.problems_limit.domain.model.ProblemsLimitScreen
import org.hyperskill.app.problems_limit.injection.ProblemsLimitComponent
import org.hyperskill.app.problems_limit.injection.ProblemsLimitComponentImpl
import org.hyperskill.app.products.injection.ProductsDataComponent
import org.hyperskill.app.products.injection.ProductsDataComponentImpl
import org.hyperskill.app.profile.injection.ProfileComponent
import org.hyperskill.app.profile.injection.ProfileComponentImpl
import org.hyperskill.app.profile.injection.ProfileDataComponent
import org.hyperskill.app.profile.injection.ProfileDataComponentImpl
import org.hyperskill.app.profile_settings.injection.ProfileSettingsComponent
import org.hyperskill.app.profile_settings.injection.ProfileSettingsComponentImpl
import org.hyperskill.app.progress_screen.injection.ProgressScreenComponent
import org.hyperskill.app.progress_screen.injection.ProgressScreenComponentImpl
import org.hyperskill.app.progresses.injection.ProgressesDataComponent
import org.hyperskill.app.progresses.injection.ProgressesDataComponentImpl
import org.hyperskill.app.progresses.injection.ProgressesFlowDataComponent
import org.hyperskill.app.progresses.injection.ProgressesFlowDataComponentImpl
import org.hyperskill.app.project_selection.details.injection.ProjectSelectionDetailsComponent
import org.hyperskill.app.project_selection.details.injection.ProjectSelectionDetailsComponentImpl
import org.hyperskill.app.project_selection.list.injection.ProjectSelectionListComponent
import org.hyperskill.app.project_selection.list.injection.ProjectSelectionListComponentImpl
import org.hyperskill.app.projects.injection.ProjectsDataComponent
import org.hyperskill.app.projects.injection.ProjectsDataComponentImpl
import org.hyperskill.app.providers.injection.ProvidersDataComponent
import org.hyperskill.app.providers.injection.ProvidersDataComponentImpl
import org.hyperskill.app.reactions.injection.ReactionsDataComponent
import org.hyperskill.app.reactions.injection.ReactionsDataComponentImpl
import org.hyperskill.app.stage_implement.injection.StageImplementComponent
import org.hyperskill.app.stage_implement.injection.StageImplementComponentImpl
import org.hyperskill.app.stages.injection.StagesDataComponent
import org.hyperskill.app.stages.injection.StagesDataComponentImpl
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.injection.StepComponent
import org.hyperskill.app.step.injection.StepComponentImpl
import org.hyperskill.app.step.injection.StepDataComponent
import org.hyperskill.app.step.injection.StepDataComponentImpl
import org.hyperskill.app.step_completion.injection.StepCompletionComponent
import org.hyperskill.app.step_completion.injection.StepCompletionComponentImpl
import org.hyperskill.app.step_completion.injection.StepCompletionFlowDataComponent
import org.hyperskill.app.step_completion.injection.StepCompletionFlowDataComponentImpl
import org.hyperskill.app.step_quiz.injection.StepQuizComponent
import org.hyperskill.app.step_quiz.injection.StepQuizComponentImpl
import org.hyperskill.app.step_quiz.injection.SubmissionDataComponent
import org.hyperskill.app.step_quiz.injection.SubmissionDataComponentImpl
import org.hyperskill.app.step_quiz_hints.injection.StepQuizHintsComponent
import org.hyperskill.app.step_quiz_hints.injection.StepQuizHintsComponentImpl
import org.hyperskill.app.streak_recovery.injection.StreakRecoveryComponent
import org.hyperskill.app.streak_recovery.injection.StreakRecoveryComponentImpl
import org.hyperskill.app.streaks.injection.StreakFlowDataComponent
import org.hyperskill.app.streaks.injection.StreakFlowDataComponentImpl
import org.hyperskill.app.streaks.injection.StreaksDataComponent
import org.hyperskill.app.streaks.injection.StreaksDataComponentImpl
import org.hyperskill.app.study_plan.injection.StudyPlanDataComponent
import org.hyperskill.app.study_plan.injection.StudyPlanDataComponentImpl
import org.hyperskill.app.study_plan.screen.injection.StudyPlanScreenComponent
import org.hyperskill.app.study_plan.screen.injection.StudyPlanScreenComponentImpl
import org.hyperskill.app.study_plan.widget.injection.StudyPlanWidgetComponent
import org.hyperskill.app.study_plan.widget.injection.StudyPlanWidgetComponentImpl
import org.hyperskill.app.topics.injection.TopicsDataComponent
import org.hyperskill.app.topics.injection.TopicsDataComponentImpl
import org.hyperskill.app.topics_repetitions.injection.TopicsRepetitionsComponent
import org.hyperskill.app.topics_repetitions.injection.TopicsRepetitionsComponentImpl
import org.hyperskill.app.topics_repetitions.injection.TopicsRepetitionsDataComponent
import org.hyperskill.app.topics_repetitions.injection.TopicsRepetitionsDataComponentImpl
import org.hyperskill.app.topics_repetitions.injection.TopicsRepetitionsFlowDataComponent
import org.hyperskill.app.topics_repetitions.injection.TopicsRepetitionsFlowDataComponentImpl
import org.hyperskill.app.track.injection.TrackDataComponent
import org.hyperskill.app.track.injection.TrackDataComponentImpl
import org.hyperskill.app.track_selection.details.injection.TrackSelectionDetailsComponent
import org.hyperskill.app.track_selection.details.injection.TrackSelectionDetailsComponentImpl
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListComponent
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListComponentImpl
import org.hyperskill.app.user_storage.injection.UserStorageComponent
import org.hyperskill.app.user_storage.injection.UserStorageComponentImpl

abstract class BaseAppGraph : AppGraph {

    override val mainComponent: MainComponent by lazy {
        MainComponentImpl(this)
    }

    override val networkComponent: NetworkComponent by lazy {
        NetworkComponentImpl(this)
    }

    override val submissionDataComponent: SubmissionDataComponent by lazy {
        SubmissionDataComponentImpl(this)
    }

    override val authComponent: AuthComponent by lazy {
        AuthComponentImpl(this)
    }

    override val streakFlowDataComponent: StreakFlowDataComponent by lazy {
        StreakFlowDataComponentImpl()
    }

    override val topicsRepetitionsFlowDataComponent: TopicsRepetitionsFlowDataComponent by lazy {
        TopicsRepetitionsFlowDataComponentImpl()
    }

    override val stepCompletionFlowDataComponent: StepCompletionFlowDataComponent by lazy {
        StepCompletionFlowDataComponentImpl()
    }

    override val progressesFlowDataComponent: ProgressesFlowDataComponent by lazy {
        ProgressesFlowDataComponentImpl()
    }

    override val notificationFlowDataComponent: NotificationFlowDataComponent by lazy {
        NotificationFlowDataComponentImpl()
    }

    override val stateRepositoriesComponent: StateRepositoriesComponent by lazy {
        StateRepositoriesComponentImpl(this)
    }

    override val profileDataComponent: ProfileDataComponent by lazy {
        ProfileDataComponentImpl(
            networkComponent = networkComponent,
            commonComponent = commonComponent,
            submissionDataComponent = submissionDataComponent
        )
    }

    override val analyticComponent: AnalyticComponent by lazy {
        AnalyticComponentImpl(
            networkComponent = networkComponent,
            commonComponent = commonComponent,
            authComponent = authComponent,
            profileDataComponent = profileDataComponent,
            notificationComponent = buildNotificationComponent(),
            sentryComponent = sentryComponent
        )
    }

    override fun buildMainDataComponent(): MainDataComponent =
        MainDataComponentImpl(this)

    /**
     * Auth social component
     */
    override fun buildAuthSocialComponent(): AuthSocialComponent =
        AuthSocialComponentImpl(
            commonComponent,
            authComponent,
            profileDataComponent,
            analyticComponent,
            sentryComponent
        )

    /**
     * Auth credentials component
     */
    override fun buildAuthCredentialsComponent(): AuthCredentialsComponent =
        AuthCredentialsComponentImpl(
            commonComponent,
            authComponent,
            profileDataComponent,
            buildMagicLinksDataComponent(),
            analyticComponent,
            sentryComponent
        )

    /**
     * Step component
     */
    override fun buildStepComponent(stepRoute: StepRoute): StepComponent =
        StepComponentImpl(this, stepRoute)

    override fun buildStepDataComponent(): StepDataComponent =
        StepDataComponentImpl(this)

    /**
     * Step quiz component
     */
    override fun buildStepQuizComponent(stepRoute: StepRoute): StepQuizComponent =
        StepQuizComponentImpl(this, stepRoute)

    /**
     * Step quiz hints component
     */
    override fun buildStepQuizHintsComponent(stepRoute: StepRoute): StepQuizHintsComponent =
        StepQuizHintsComponentImpl(this, stepRoute)

    /**
     * Step completion component
     */
    override fun buildStepCompletionComponent(stepRoute: StepRoute): StepCompletionComponent =
        StepCompletionComponentImpl(this, stepRoute)

    /**
     * Stage implement component
     */
    override fun buildStageImplementComponent(projectId: Long, stageId: Long): StageImplementComponent =
        StageImplementComponentImpl(this, projectId = projectId, stageId = stageId)

    override fun buildTrackDataComponent(): TrackDataComponent =
        TrackDataComponentImpl(this)

    override fun buildProfileComponent(): ProfileComponent =
        ProfileComponentImpl(this)

    /**
     * Profile settings component
     */
    override fun buildProfileSettingsComponent(): ProfileSettingsComponent =
        ProfileSettingsComponentImpl(this)

    /**
     * Home component
     */
    override fun buildHomeComponent(): HomeComponent =
        HomeComponentImpl(this)

    /**
     * Notification component
     */
    override fun buildNotificationComponent(): NotificationComponent =
        NotificationComponentImpl(this)

    /**
     * Onboarding component
     */
    override fun buildOnboardingComponent(): OnboardingComponent =
        OnboardingComponentImpl(this)

    /**
     * Topics repetitions component
     */
    override fun buildTopicsRepetitionsComponent(): TopicsRepetitionsComponent =
        TopicsRepetitionsComponentImpl(this)

    override fun buildTopicsRepetitionsDataComponent(): TopicsRepetitionsDataComponent =
        TopicsRepetitionsDataComponentImpl(this)

    /**
     * Debug component
     */
    override fun buildDebugComponent(): DebugComponent =
        DebugComponentImpl(this)

    /**
     * ProblemsLimit component
     */
    override fun buildProblemsLimitComponent(screen: ProblemsLimitScreen): ProblemsLimitComponent =
        ProblemsLimitComponentImpl(screen, this)

    /**
     * Study plan component
     */
    override fun buildStudyPlanWidgetComponent(): StudyPlanWidgetComponent =
        StudyPlanWidgetComponentImpl(this)

    /**
     * Project selection list component
     */
    override fun buildProjectSelectionListComponent(): ProjectSelectionListComponent =
        ProjectSelectionListComponentImpl(this)

    /**
     * Project selection details component
     */
    override fun buildProjectSelectionDetailsComponent(): ProjectSelectionDetailsComponent =
        ProjectSelectionDetailsComponentImpl(this)

    /**
     * Track selection list component
     */
    override fun buildTrackSelectionListComponent(): TrackSelectionListComponent =
        TrackSelectionListComponentImpl(this)

    /**
     * Track selection details component
     */
    override fun buildTrackSelectionDetailsComponent(): TrackSelectionDetailsComponent =
        TrackSelectionDetailsComponentImpl(this)

    /**
     * Next learning activity widget component
     */
    override fun buildNextLearningActivityWidgetComponent(): NextLearningActivityWidgetComponent =
        NextLearningActivityWidgetComponentImpl(this)

    override fun buildStudyPlanScreenComponent(): StudyPlanScreenComponent =
        StudyPlanScreenComponentImpl(this)

    override fun buildStudyPlanDataComponent(): StudyPlanDataComponent =
        StudyPlanDataComponentImpl(this)

    override fun buildUserStorageComponent(): UserStorageComponent =
        UserStorageComponentImpl(this)

    override fun buildCommentsDataComponent(): CommentsDataComponent =
        CommentsDataComponentImpl(this)

    override fun buildMagicLinksDataComponent(): MagicLinksDataComponent =
        MagicLinksDataComponentImpl(this)

    override fun buildDiscussionsDataComponent(): DiscussionsDataComponent =
        DiscussionsDataComponentImpl(this)

    override fun buildReactionsDataComponent(): ReactionsDataComponent =
        ReactionsDataComponentImpl(this)

    override fun buildLikesDataComponent(): LikesDataComponent =
        LikesDataComponentImpl(this)

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

    override fun buildGamificationToolbarComponent(screen: GamificationToolbarScreen): GamificationToolbarComponent =
        GamificationToolbarComponentImpl(this, screen)

    override fun buildProjectsDataComponent(): ProjectsDataComponent =
        ProjectsDataComponentImpl(this)

    override fun buildStagesDataComponent(): StagesDataComponent =
        StagesDataComponentImpl(this)

    override fun buildFreemiumDataComponent(): FreemiumDataComponent =
        FreemiumDataComponentImpl(this)

    override fun buildProvidersDataComponent(): ProvidersDataComponent =
        ProvidersDataComponentImpl(this)

    override fun buildStreakRecoveryComponent(): StreakRecoveryComponent =
        StreakRecoveryComponentImpl(this)

    override fun buildDevicesDataComponent(): DevicesDataComponent =
        DevicesDataComponentImpl(this)

    override fun buildPushNotificationsComponent(): PushNotificationsComponent =
        PushNotificationsComponentImpl(this)

    override fun buildClickedNotificationComponent(): NotificationClickHandlingComponent =
        NotificationClickHandlingComponentImpl(this)

    override fun buildProgressScreenComponent(): ProgressScreenComponent =
        ProgressScreenComponentImpl(this)

    override fun buildBadgesDataComponent(): BadgesDataComponent =
        BadgesDataComponentImpl(this)

    override fun buildNotificationsOnboardingComponent(): NotificationsOnboardingComponent =
        NotificationsOnboardingComponentImpl(this)
}