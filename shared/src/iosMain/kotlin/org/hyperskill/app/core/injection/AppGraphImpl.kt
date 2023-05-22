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
import org.hyperskill.app.notification.injection.NotificationComponent
import org.hyperskill.app.notification.injection.NotificationComponentImpl
import org.hyperskill.app.notification.injection.NotificationFlowDataComponent
import org.hyperskill.app.notification.injection.NotificationFlowDataComponentImpl
import org.hyperskill.app.onboarding.injection.OnboardingComponent
import org.hyperskill.app.onboarding.injection.OnboardingComponentImpl
import org.hyperskill.app.placeholder_new_user.injection.PlaceholderNewUserComponent
import org.hyperskill.app.placeholder_new_user.injection.PlaceholderNewUserComponentImpl
import org.hyperskill.app.problems_limit.domain.model.ProblemsLimitScreen
import org.hyperskill.app.problems_limit.injection.ProblemsLimitComponent
import org.hyperskill.app.problems_limit.injection.ProblemsLimitComponentImpl
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
import org.hyperskill.app.progresses.injection.ProgressesFlowDataComponent
import org.hyperskill.app.progresses.injection.ProgressesFlowDataComponentImpl
import org.hyperskill.app.project_selection.injection.ProjectSelectionListComponent
import org.hyperskill.app.project_selection.injection.ProjectSelectionListComponentImpl
import org.hyperskill.app.projects.injection.ProjectsDataComponent
import org.hyperskill.app.projects.injection.ProjectsDataComponentImpl
import org.hyperskill.app.reactions.injection.ReactionsDataComponent
import org.hyperskill.app.reactions.injection.ReactionsDataComponentImpl
import org.hyperskill.app.sentry.domain.model.manager.SentryManager
import org.hyperskill.app.sentry.injection.SentryComponent
import org.hyperskill.app.sentry.injection.SentryComponentImpl
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
import org.hyperskill.app.topics_to_discover_next.domain.model.TopicsToDiscoverNextScreen
import org.hyperskill.app.topics_to_discover_next.injection.TopicsToDiscoverNextComponent
import org.hyperskill.app.topics_to_discover_next.injection.TopicsToDiscoverNextComponentImpl
import org.hyperskill.app.topics_to_discover_next.injection.TopicsToDiscoverNextDataComponent
import org.hyperskill.app.topics_to_discover_next.injection.TopicsToDiscoverNextDataComponentImpl
import org.hyperskill.app.track.injection.TrackComponent
import org.hyperskill.app.track.injection.TrackComponentImpl
import org.hyperskill.app.track.injection.TrackDataComponent
import org.hyperskill.app.track.injection.TrackDataComponentImpl
import org.hyperskill.app.track_selection.details.injection.TrackSelectionDetailsComponent
import org.hyperskill.app.track_selection.details.injection.TrackSelectionDetailsComponentImpl
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListComponent
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListComponentImpl
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

    override val streakFlowDataComponent: StreakFlowDataComponent =
        StreakFlowDataComponentImpl()

    override val topicsRepetitionsFlowDataComponent: TopicsRepetitionsFlowDataComponent =
        TopicsRepetitionsFlowDataComponentImpl()

    override val stepCompletionFlowDataComponent: StepCompletionFlowDataComponent =
        StepCompletionFlowDataComponentImpl()

    override val progressesFlowDataComponent: ProgressesFlowDataComponent =
        ProgressesFlowDataComponentImpl()

    override val notificationFlowDataComponent: NotificationFlowDataComponent =
        NotificationFlowDataComponentImpl()

    override val stateRepositoriesComponent: StateRepositoriesComponent =
        StateRepositoriesComponentImpl(this)

    override val sentryComponent: SentryComponent =
        SentryComponentImpl(sentryManager)

    override val analyticComponent: AnalyticComponent =
        AnalyticComponentImpl(this)

    override val mainComponent: MainComponent =
        MainComponentImpl(this)

    override fun buildMainDataComponent(): MainDataComponent =
        MainDataComponentImpl(this)

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

    override fun buildStepComponent(stepRoute: StepRoute): StepComponent =
        StepComponentImpl(this, stepRoute)

    override fun buildStepDataComponent(): StepDataComponent =
        StepDataComponentImpl(this)

    override fun buildStepQuizComponent(stepRoute: StepRoute): StepQuizComponent =
        StepQuizComponentImpl(this, stepRoute)

    override fun buildStepQuizHintsComponent(stepRoute: StepRoute): StepQuizHintsComponent =
        StepQuizHintsComponentImpl(this, stepRoute)

    override fun buildStepCompletionComponent(stepRoute: StepRoute): StepCompletionComponent =
        StepCompletionComponentImpl(this, stepRoute)

    override fun buildStageImplementComponent(projectId: Long, stageId: Long): StageImplementComponent =
        StageImplementComponentImpl(this, projectId = projectId, stageId = stageId)

    override fun buildProfileDataComponent(): ProfileDataComponent =
        ProfileDataComponentImpl(this)

    override fun buildTrackComponent(): TrackComponent =
        TrackComponentImpl(this)

    override fun buildTrackDataComponent(): TrackDataComponent =
        TrackDataComponentImpl(this)

    override fun buildTrackSelectionListComponent(): TrackSelectionListComponent =
        TrackSelectionListComponentImpl(this)

    override fun buildTrackSelectionDetailsComponent(): TrackSelectionDetailsComponent =
        TrackSelectionDetailsComponentImpl(this)

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

    override fun buildTopicsRepetitionsDataComponent(): TopicsRepetitionsDataComponent =
        TopicsRepetitionsDataComponentImpl(this)

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

    override fun buildGamificationToolbarComponent(screen: GamificationToolbarScreen): GamificationToolbarComponent =
        GamificationToolbarComponentImpl(this, screen)

    override fun buildTopicsToDiscoverNextComponent(screen: TopicsToDiscoverNextScreen): TopicsToDiscoverNextComponent =
        TopicsToDiscoverNextComponentImpl(this, screen)

    override fun buildTopicsToDiscoverNextDataComponent(): TopicsToDiscoverNextDataComponent =
        TopicsToDiscoverNextDataComponentImpl(this)

    override fun buildStudyPlanDataComponent(): StudyPlanDataComponent =
        StudyPlanDataComponentImpl(this)

    override fun buildProjectsDataComponent(): ProjectsDataComponent =
        ProjectsDataComponentImpl(this)

    override fun buildProjectSelectionListComponent(): ProjectSelectionListComponent =
        ProjectSelectionListComponentImpl(this)

    override fun buildStagesDataComponent(): StagesDataComponent =
        StagesDataComponentImpl(this)

    override fun buildStudyPlanWidgetComponent(): StudyPlanWidgetComponent =
        StudyPlanWidgetComponentImpl(this)

    override fun buildStudyPlanScreenComponent(): StudyPlanScreenComponent =
        StudyPlanScreenComponentImpl(this)

    override fun buildFreemiumDataComponent(): FreemiumDataComponent =
        FreemiumDataComponentImpl(this)

    override fun buildProblemsLimitComponent(screen: ProblemsLimitScreen): ProblemsLimitComponent =
        ProblemsLimitComponentImpl(screen, this)
}