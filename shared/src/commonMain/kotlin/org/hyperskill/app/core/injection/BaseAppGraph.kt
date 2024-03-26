package org.hyperskill.app.core.injection

import org.hyperskill.app.analytic.injection.HyperskillAnalyticEngineComponent
import org.hyperskill.app.analytic.injection.HyperskillAnalyticEngineComponentImpl
import org.hyperskill.app.auth.injection.AuthComponent
import org.hyperskill.app.auth.injection.AuthComponentImpl
import org.hyperskill.app.auth.injection.AuthCredentialsComponent
import org.hyperskill.app.auth.injection.AuthCredentialsComponentImpl
import org.hyperskill.app.auth.injection.AuthSocialComponent
import org.hyperskill.app.auth.injection.AuthSocialComponentImpl
import org.hyperskill.app.badges.injection.BadgesDataComponent
import org.hyperskill.app.badges.injection.BadgesDataComponentImpl
import org.hyperskill.app.challenges.injection.ChallengesDataComponent
import org.hyperskill.app.challenges.injection.ChallengesDataComponentImpl
import org.hyperskill.app.challenges.widget.injection.ChallengeWidgetComponent
import org.hyperskill.app.challenges.widget.injection.ChallengeWidgetComponentImpl
import org.hyperskill.app.comments.injection.CommentsDataComponent
import org.hyperskill.app.comments.injection.CommentsDataComponentImpl
import org.hyperskill.app.debug.injection.DebugComponent
import org.hyperskill.app.debug.injection.DebugComponentImpl
import org.hyperskill.app.devices.injection.DevicesDataComponent
import org.hyperskill.app.devices.injection.DevicesDataComponentImpl
import org.hyperskill.app.discussions.injection.DiscussionsDataComponent
import org.hyperskill.app.discussions.injection.DiscussionsDataComponentImpl
import org.hyperskill.app.first_problem_onboarding.injection.FirstProblemOnboardingComponent
import org.hyperskill.app.first_problem_onboarding.injection.FirstProblemOnboardingComponentImpl
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponent
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponentImpl
import org.hyperskill.app.home.injection.HomeComponent
import org.hyperskill.app.home.injection.HomeComponentImpl
import org.hyperskill.app.items.injection.ItemsDataComponent
import org.hyperskill.app.items.injection.ItemsDataComponentImpl
import org.hyperskill.app.leaderboard.injection.LeaderboardDataComponent
import org.hyperskill.app.leaderboard.injection.LeaderboardDataComponentImpl
import org.hyperskill.app.leaderboard.screen.injection.LeaderboardScreenComponent
import org.hyperskill.app.leaderboard.screen.injection.LeaderboardScreenComponentImpl
import org.hyperskill.app.leaderboard.widget.injection.LeaderboardWidgetComponent
import org.hyperskill.app.leaderboard.widget.injection.LeaderboardWidgetComponentImpl
import org.hyperskill.app.learning_activities.injection.LearningActivitiesDataComponent
import org.hyperskill.app.learning_activities.injection.LearningActivitiesDataComponentImpl
import org.hyperskill.app.likes.injection.LikesDataComponent
import org.hyperskill.app.likes.injection.LikesDataComponentImpl
import org.hyperskill.app.logging.inject.LoggerComponent
import org.hyperskill.app.logging.inject.LoggerComponentImpl
import org.hyperskill.app.magic_links.injection.MagicLinksDataComponent
import org.hyperskill.app.magic_links.injection.MagicLinksDataComponentImpl
import org.hyperskill.app.main.injection.MainComponent
import org.hyperskill.app.main.injection.MainComponentImpl
import org.hyperskill.app.main.injection.MainDataComponent
import org.hyperskill.app.main.injection.MainDataComponentImpl
import org.hyperskill.app.manage_subscription.injection.ManageSubscriptionComponent
import org.hyperskill.app.manage_subscription.injection.ManageSubscriptionComponentImpl
import org.hyperskill.app.network.injection.NetworkComponent
import org.hyperskill.app.network.injection.NetworkComponentImpl
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
import org.hyperskill.app.onboarding.injection.OnboardingDataComponent
import org.hyperskill.app.onboarding.injection.OnboardingDataComponentImpl
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.paywall.injection.PaywallComponent
import org.hyperskill.app.paywall.injection.PaywallComponentImpl
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
import org.hyperskill.app.request_review.injection.RequestReviewDataComponent
import org.hyperskill.app.request_review.injection.RequestReviewDataComponentImpl
import org.hyperskill.app.request_review.modal.injection.RequestReviewModalComponent
import org.hyperskill.app.request_review.modal.injection.RequestReviewModalComponentImpl
import org.hyperskill.app.search.injection.SearchComponent
import org.hyperskill.app.search.injection.SearchComponentImpl
import org.hyperskill.app.search_results.injection.SearchResultsDataComponent
import org.hyperskill.app.search_results.injection.SearchResultsDataComponentImpl
import org.hyperskill.app.share_streak.injection.ShareStreakDataComponent
import org.hyperskill.app.share_streak.injection.ShareStreakDataComponentImpl
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
import org.hyperskill.app.study_plan.screen.injection.StudyPlanScreenComponent
import org.hyperskill.app.study_plan.screen.injection.StudyPlanScreenComponentImpl
import org.hyperskill.app.study_plan.widget.injection.StudyPlanWidgetComponent
import org.hyperskill.app.study_plan.widget.injection.StudyPlanWidgetComponentImpl
import org.hyperskill.app.subscriptions.injection.SubscriptionsDataComponent
import org.hyperskill.app.subscriptions.injection.SubscriptionsDataComponentImpl
import org.hyperskill.app.theory_feedback.injection.TheoryFeedbackComponent
import org.hyperskill.app.theory_feedback.injection.TheoryFeedbackComponentImpl
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
import org.hyperskill.app.users_questionnaire.injection.UsersQuestionnaireDataComponent
import org.hyperskill.app.users_questionnaire.injection.UsersQuestionnaireDataComponentImpl
import org.hyperskill.app.users_questionnaire.onboarding.injection.UsersQuestionnaireOnboardingComponent
import org.hyperskill.app.users_questionnaire.onboarding.injection.UsersQuestionnaireOnboardingComponentImpl
import org.hyperskill.app.users_questionnaire.widget.injection.UsersQuestionnaireWidgetComponent
import org.hyperskill.app.users_questionnaire.widget.injection.UsersQuestionnaireWidgetComponentImpl
import org.hyperskill.app.welcome.injection.WelcomeComponent
import org.hyperskill.app.welcome.injection.WelcomeComponentImpl
import org.hyperskill.app.welcome.injection.WelcomeDataComponent
import org.hyperskill.app.welcome.injection.WelcomeDataComponentImpl
import org.hyperskill.app.welcome_onboarding.injection.WelcomeOnboardingComponent
import org.hyperskill.app.welcome_onboarding.injection.WelcomeOnboardingComponentImpl

abstract class BaseAppGraph : AppGraph {

    override val mainComponent: MainComponent by lazy {
        MainComponentImpl(this)
    }

    override val networkComponent: NetworkComponent by lazy {
        NetworkComponentImpl(this)
    }

    override val loggerComponent: LoggerComponent by lazy {
        LoggerComponentImpl(this)
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
            commonComponent = commonComponent
        )
    }

    override val subscriptionDataComponent: SubscriptionsDataComponent by lazy {
        SubscriptionsDataComponentImpl(this)
    }

    override fun buildHyperskillAnalyticEngineComponent(): HyperskillAnalyticEngineComponent =
        HyperskillAnalyticEngineComponentImpl(this)

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
            sentryComponent,
            loggerComponent
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
            sentryComponent,
            loggerComponent
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

    override fun buildSubmissionDataComponent(): SubmissionDataComponent =
        SubmissionDataComponentImpl(this)

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
     * Welcome component
     */
    override fun buildWelcomeComponent(): WelcomeComponent =
        WelcomeComponentImpl(this)

    override fun buildWelcomeDataComponent(): WelcomeDataComponent =
        WelcomeDataComponentImpl(this)

    /**
     * Onboarding component
     */
    override fun buildOnboardingDataComponent(): OnboardingDataComponent =
        OnboardingDataComponentImpl(this)

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

    override fun buildStudyPlanScreenComponent(): StudyPlanScreenComponent =
        StudyPlanScreenComponentImpl(this)

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

    override fun buildFirstProblemOnboardingComponent(): FirstProblemOnboardingComponent =
        FirstProblemOnboardingComponentImpl(this)

    override fun buildShareStreakDataComponent(): ShareStreakDataComponent =
        ShareStreakDataComponentImpl(this)

    override fun buildChallengesDataComponent(): ChallengesDataComponent =
        ChallengesDataComponentImpl(this)

    override fun buildChallengeWidgetComponent(): ChallengeWidgetComponent =
        ChallengeWidgetComponentImpl(this)

    override fun buildLeaderboardDataComponent(): LeaderboardDataComponent =
        LeaderboardDataComponentImpl(this)

    override fun buildLeaderboardScreenComponent(): LeaderboardScreenComponent =
        LeaderboardScreenComponentImpl(this)

    override fun buildLeaderboardWidgetComponent(): LeaderboardWidgetComponent =
        LeaderboardWidgetComponentImpl(this)

    override fun buildSearchResultsDataComponent(): SearchResultsDataComponent =
        SearchResultsDataComponentImpl(this)

    override fun buildSearchComponent(): SearchComponent =
        SearchComponentImpl(this)

    override fun buildWelcomeOnboardingComponent(): WelcomeOnboardingComponent =
        WelcomeOnboardingComponentImpl(this)

    override fun buildRequestReviewDataComponent(): RequestReviewDataComponent =
        RequestReviewDataComponentImpl(this)

    override fun buildRequestReviewModalComponent(stepRoute: StepRoute): RequestReviewModalComponent =
        RequestReviewModalComponentImpl(appGraph = this, stepRoute = stepRoute)

    override fun buildPaywallComponent(
        paywallTransitionSource: PaywallTransitionSource
    ): PaywallComponent =
        PaywallComponentImpl(paywallTransitionSource, this)

    override fun buildManageSubscriptionComponent(): ManageSubscriptionComponent =
        ManageSubscriptionComponentImpl(this)

    override fun buildUsersQuestionnaireDataComponent(): UsersQuestionnaireDataComponent =
        UsersQuestionnaireDataComponentImpl(this)

    override fun buildUsersQuestionnaireWidgetComponent(): UsersQuestionnaireWidgetComponent =
        UsersQuestionnaireWidgetComponentImpl(this)

    override fun buildUsersQuestionnaireOnboardingComponent(): UsersQuestionnaireOnboardingComponent =
        UsersQuestionnaireOnboardingComponentImpl(this)

    override fun buildTheoryFeedbackComponent(stepRoute: StepRoute): TheoryFeedbackComponent =
        TheoryFeedbackComponentImpl(this, stepRoute)
}