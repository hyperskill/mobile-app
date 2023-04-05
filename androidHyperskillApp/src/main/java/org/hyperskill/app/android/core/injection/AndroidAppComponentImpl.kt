package org.hyperskill.app.android.core.injection

import android.app.Application
import android.content.Context
import org.hyperskill.app.analytic.injection.AnalyticComponent
import org.hyperskill.app.analytic.injection.AnalyticComponentImpl
import org.hyperskill.app.android.code.injection.PlatformCodeEditorComponent
import org.hyperskill.app.android.code.injection.PlatformCodeEditorComponentImpl
import org.hyperskill.app.android.image_loading.injection.ImageLoadingComponent
import org.hyperskill.app.android.image_loading.injection.ImageLoadingComponentImpl
import org.hyperskill.app.android.latex.injection.PlatformLatexComponent
import org.hyperskill.app.android.latex.injection.PlatformLatexComponentImpl
import org.hyperskill.app.android.main.injection.NavigationComponent
import org.hyperskill.app.android.main.injection.NavigationComponentImpl
import org.hyperskill.app.android.notification.injection.PlatformNotificationComponent
import org.hyperskill.app.android.notification.injection.PlatformNotificationComponentImpl
import org.hyperskill.app.android.sentry.domain.model.manager.SentryManagerImpl
import org.hyperskill.app.auth.injection.AuthComponent
import org.hyperskill.app.auth.injection.AuthComponentImpl
import org.hyperskill.app.auth.injection.AuthCredentialsComponent
import org.hyperskill.app.auth.injection.AuthCredentialsComponentImpl
import org.hyperskill.app.auth.injection.AuthSocialComponent
import org.hyperskill.app.auth.injection.AuthSocialComponentImpl
import org.hyperskill.app.auth.injection.PlatformAuthCredentialsComponent
import org.hyperskill.app.auth.injection.PlatformAuthCredentialsComponentImpl
import org.hyperskill.app.auth.injection.PlatformAuthSocialComponent
import org.hyperskill.app.auth.injection.PlatformAuthSocialComponentImpl
import org.hyperskill.app.auth.injection.PlatformAuthSocialWebViewComponent
import org.hyperskill.app.auth.injection.PlatformAuthSocialWebViewComponentImpl
import org.hyperskill.app.comments.injection.CommentsDataComponent
import org.hyperskill.app.comments.injection.CommentsDataComponentImpl
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.injection.CommonComponent
import org.hyperskill.app.core.injection.CommonComponentImpl
import org.hyperskill.app.core.injection.StateRepositoriesComponent
import org.hyperskill.app.core.injection.StateRepositoriesComponentImpl
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.debug.injection.DebugComponent
import org.hyperskill.app.debug.injection.DebugComponentImpl
import org.hyperskill.app.debug.injection.PlatformDebugComponent
import org.hyperskill.app.debug.injection.PlatformDebugComponentImpl
import org.hyperskill.app.discussions.injection.DiscussionsDataComponent
import org.hyperskill.app.discussions.injection.DiscussionsDataComponentImpl
import org.hyperskill.app.freemium.injection.FreemiumDataComponent
import org.hyperskill.app.freemium.injection.FreemiumDataComponentImpl
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponent
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponentImpl
import org.hyperskill.app.home.injection.HomeComponent
import org.hyperskill.app.home.injection.HomeComponentImpl
import org.hyperskill.app.home.injection.PlatformHomeComponent
import org.hyperskill.app.home.injection.PlatformHomeComponentImpl
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
import org.hyperskill.app.main.injection.PlatformMainComponent
import org.hyperskill.app.main.injection.PlatformMainComponentImpl
import org.hyperskill.app.network.injection.NetworkComponent
import org.hyperskill.app.network.injection.NetworkComponentImpl
import org.hyperskill.app.notification.injection.NotificationComponent
import org.hyperskill.app.notification.injection.NotificationComponentImpl
import org.hyperskill.app.notification.injection.NotificationFlowDataComponent
import org.hyperskill.app.notification.injection.NotificationFlowDataComponentImpl
import org.hyperskill.app.onboarding.injection.OnboardingComponent
import org.hyperskill.app.onboarding.injection.OnboardingComponentImpl
import org.hyperskill.app.onboarding.injection.PlatformOnboardingComponent
import org.hyperskill.app.onboarding.injection.PlatformOnboardingComponentImpl
import org.hyperskill.app.placeholder_new_user.injection.PlaceholderNewUserComponent
import org.hyperskill.app.placeholder_new_user.injection.PlaceholderNewUserComponentImpl
import org.hyperskill.app.placeholder_new_user.injection.PlatformPlaceholderNewUserComponent
import org.hyperskill.app.placeholder_new_user.injection.PlatformPlaceholderNewUserComponentImpl
import org.hyperskill.app.problems_limit.injection.PlatformProblemsLimitComponent
import org.hyperskill.app.problems_limit.injection.PlatformProblemsLimitComponentImpl
import org.hyperskill.app.problems_limit.injection.ProblemsLimitComponent
import org.hyperskill.app.problems_limit.injection.ProblemsLimitComponentImpl
import org.hyperskill.app.products.injection.ProductsDataComponent
import org.hyperskill.app.products.injection.ProductsDataComponentImpl
import org.hyperskill.app.profile.injection.PlatformProfileComponent
import org.hyperskill.app.profile.injection.PlatformProfileComponentImpl
import org.hyperskill.app.profile.injection.PlatformProfileSettingsComponentImpl
import org.hyperskill.app.profile.injection.ProfileComponent
import org.hyperskill.app.profile.injection.ProfileComponentImpl
import org.hyperskill.app.profile.injection.ProfileDataComponent
import org.hyperskill.app.profile.injection.ProfileDataComponentImpl
import org.hyperskill.app.profile.injection.ProfileHypercoinsDataComponent
import org.hyperskill.app.profile.injection.ProfileHypercoinsDataComponentImpl
import org.hyperskill.app.profile_settings.injection.PlatformProfileSettingsComponent
import org.hyperskill.app.profile_settings.injection.ProfileSettingsComponent
import org.hyperskill.app.profile_settings.injection.ProfileSettingsComponentImpl
import org.hyperskill.app.progresses.injection.ProgressesDataComponent
import org.hyperskill.app.progresses.injection.ProgressesDataComponentImpl
import org.hyperskill.app.progresses.injection.ProgressesFlowDataComponent
import org.hyperskill.app.progresses.injection.ProgressesFlowDataComponentImpl
import org.hyperskill.app.projects.injection.ProjectsDataComponent
import org.hyperskill.app.projects.injection.ProjectsDataComponentImpl
import org.hyperskill.app.reactions.injection.ReactionsDataComponent
import org.hyperskill.app.reactions.injection.ReactionsDataComponentImpl
import org.hyperskill.app.sentry.injection.SentryComponent
import org.hyperskill.app.sentry.injection.SentryComponentImpl
import org.hyperskill.app.stage_implement.injection.StageImplementComponent
import org.hyperskill.app.stage_implement.injection.StageImplementComponentImpl
import org.hyperskill.app.stage_implementation.injection.PlatformStageImplementationComponent
import org.hyperskill.app.stage_implementation.injection.PlatformStageImplementationComponentImpl
import org.hyperskill.app.stages.injection.StagesDataComponent
import org.hyperskill.app.stages.injection.StagesDataComponentImpl
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.injection.PlatformStepComponent
import org.hyperskill.app.step.injection.PlatformStepComponentImpl
import org.hyperskill.app.step.injection.StepComponent
import org.hyperskill.app.step.injection.StepComponentImpl
import org.hyperskill.app.step.injection.StepDataComponent
import org.hyperskill.app.step.injection.StepDataComponentImpl
import org.hyperskill.app.step_completion.injection.StepCompletionComponent
import org.hyperskill.app.step_completion.injection.StepCompletionComponentImpl
import org.hyperskill.app.step_completion.injection.StepCompletionFlowDataComponent
import org.hyperskill.app.step_completion.injection.StepCompletionFlowDataComponentImpl
import org.hyperskill.app.step_quiz.injection.PlatformStepQuizComponent
import org.hyperskill.app.step_quiz.injection.PlatformStepQuizComponentImpl
import org.hyperskill.app.step_quiz.injection.StepQuizComponent
import org.hyperskill.app.step_quiz.injection.StepQuizComponentImpl
import org.hyperskill.app.step_quiz.injection.SubmissionDataComponent
import org.hyperskill.app.step_quiz.injection.SubmissionDataComponentImpl
import org.hyperskill.app.step_quiz_hints.injection.PlatformStepQuizHintsComponent
import org.hyperskill.app.step_quiz_hints.injection.PlatformStepQuizHintsComponentImpl
import org.hyperskill.app.step_quiz_hints.injection.StepQuizHintsComponent
import org.hyperskill.app.step_quiz_hints.injection.StepQuizHintsComponentImpl
import org.hyperskill.app.streaks.injection.StreakFlowDataComponentImpl
import org.hyperskill.app.streaks.injection.StreaksDataComponent
import org.hyperskill.app.streaks.injection.StreaksDataComponentImpl
import org.hyperskill.app.study_plan.injection.StudyPlanDataComponent
import org.hyperskill.app.study_plan.injection.StudyPlanDataComponentImpl
import org.hyperskill.app.topics.injection.TopicsDataComponent
import org.hyperskill.app.topics.injection.TopicsDataComponentImpl
import org.hyperskill.app.topics_repetitions.injection.PlatformTopicsRepetitionComponent
import org.hyperskill.app.topics_repetitions.injection.PlatformTopicsRepetitionComponentImpl
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
import org.hyperskill.app.track.injection.PlatformTrackComponent
import org.hyperskill.app.track.injection.PlatformTrackComponentImpl
import org.hyperskill.app.track.injection.TrackComponent
import org.hyperskill.app.track.injection.TrackComponentImpl
import org.hyperskill.app.track.injection.TrackDataComponent
import org.hyperskill.app.track.injection.TrackDataComponentImpl
import org.hyperskill.app.user_storage.injection.UserStorageComponent
import org.hyperskill.app.user_storage.injection.UserStorageComponentImpl

class AndroidAppComponentImpl(
    private val application: Application,
    userAgentInfo: UserAgentInfo,
    buildVariant: BuildVariant
) : AndroidAppComponent {
    override val context: Context
        get() = application

    override val commonComponent: CommonComponent =
        CommonComponentImpl(application, buildVariant, userAgentInfo)

    override val mainComponent: MainComponent =
        MainComponentImpl(this)

    override val platformMainComponent: PlatformMainComponent =
        PlatformMainComponentImpl(mainComponent)

    override val imageLoadingComponent: ImageLoadingComponent =
        ImageLoadingComponentImpl(context)

    override val networkComponent: NetworkComponent =
        NetworkComponentImpl(this)

    override val submissionDataComponent: SubmissionDataComponent =
        SubmissionDataComponentImpl(this)

    override val authComponent: AuthComponent =
        AuthComponentImpl(this)

    override val navigationComponent: NavigationComponent =
        NavigationComponentImpl()

    override val profileHypercoinsDataComponent: ProfileHypercoinsDataComponent =
        ProfileHypercoinsDataComponentImpl()

    override val streakFlowDataComponent: StreakFlowDataComponentImpl =
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
        SentryComponentImpl(SentryManagerImpl(commonComponent.buildKonfig))

    override val analyticComponent: AnalyticComponent =
        AnalyticComponentImpl(this)

    override val platformNotificationComponent: PlatformNotificationComponent =
        PlatformNotificationComponentImpl(application, this)

    override fun buildPlatformAuthSocialWebViewComponent(): PlatformAuthSocialWebViewComponent =
        PlatformAuthSocialWebViewComponentImpl()

    override fun buildMainDataComponent(): MainDataComponent =
        MainDataComponentImpl(this)

    /**
     * Auth social component
     */
    override fun buildAuthSocialComponent(): AuthSocialComponent =
        AuthSocialComponentImpl(
            commonComponent,
            authComponent,
            buildProfileDataComponent(),
            analyticComponent,
            sentryComponent
        )

    override fun buildPlatformAuthSocialComponent(authSocialComponent: AuthSocialComponent): PlatformAuthSocialComponent =
        PlatformAuthSocialComponentImpl(authSocialComponent)

    /**
     * Auth credentials component
     */
    override fun buildAuthCredentialsComponent(): AuthCredentialsComponent =
        AuthCredentialsComponentImpl(
            commonComponent,
            authComponent,
            buildProfileDataComponent(),
            buildMagicLinksDataComponent(),
            analyticComponent,
            sentryComponent
        )

    override fun buildPlatformAuthCredentialsComponent(authCredentialsComponent: AuthCredentialsComponent): PlatformAuthCredentialsComponent =
        PlatformAuthCredentialsComponentImpl(authCredentialsComponent)

    /**
     * Step component
     */
    override fun buildStepComponent(stepRoute: StepRoute): StepComponent =
        StepComponentImpl(this, stepRoute)

    override fun buildPlatformStepComponent(stepComponent: StepComponent): PlatformStepComponent =
        PlatformStepComponentImpl(stepComponent)

    override fun buildStepDataComponent(): StepDataComponent =
        StepDataComponentImpl(this)

    /**
     * Step quiz component
     */
    override fun buildStepQuizComponent(stepRoute: StepRoute): StepQuizComponent =
        StepQuizComponentImpl(this, stepRoute)

    override fun buildPlatformStepQuizComponent(stepQuizComponent: StepQuizComponent): PlatformStepQuizComponent =
        PlatformStepQuizComponentImpl(stepQuizComponent)

    /**
     * Latex component
     */
    override fun buildPlatformLatexComponent(): PlatformLatexComponent =
        PlatformLatexComponentImpl(application, networkComponent.endpointConfigInfo)

    /**
     * CodeEditor component
     */
    override fun buildPlatformCodeEditorComponent(): PlatformCodeEditorComponent =
        PlatformCodeEditorComponentImpl(application)

    /**
     * Step quiz hints component
     */
    override fun buildStepQuizHintsComponent(stepRoute: StepRoute): StepQuizHintsComponent =
        StepQuizHintsComponentImpl(this, stepRoute)

    override fun buildPlatformStepQuizHintsComponent(stepRoute: StepRoute, step: Step): PlatformStepQuizHintsComponent =
        PlatformStepQuizHintsComponentImpl(this, stepRoute, step)

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

    override fun buildPlatformStageImplementationComponent(
        projectId: Long,
        stageId: Long
    ): PlatformStageImplementationComponent =
        PlatformStageImplementationComponentImpl(
            projectId = projectId,
            stageId = stageId,
            stageImplementationComponent = buildStageImplementComponent(projectId, stageId)
        )

    /**
     * Track component
     */
    override fun buildTrackComponent(): TrackComponent =
        TrackComponentImpl(this)

    override fun buildTrackDataComponent(): TrackDataComponent =
        TrackDataComponentImpl(this)

    override fun buildPlatformTrackComponent(trackComponent: TrackComponent): PlatformTrackComponent =
        PlatformTrackComponentImpl(trackComponent)

    /**
     * Profile components
     */
    override fun buildProfileDataComponent(): ProfileDataComponent =
        ProfileDataComponentImpl(this)

    override fun buildProfileComponent(): ProfileComponent =
        ProfileComponentImpl(this)

    override fun buildPlatformProfileComponent(profileComponent: ProfileComponent): PlatformProfileComponent =
        PlatformProfileComponentImpl(profileComponent)

    /**
     * Profile settings component
     */
    override fun buildProfileSettingsComponent(): ProfileSettingsComponent =
        ProfileSettingsComponentImpl(this)

    override fun buildPlatformProfileSettingsComponent(profileSettingsComponent: ProfileSettingsComponent): PlatformProfileSettingsComponent =
        PlatformProfileSettingsComponentImpl(profileSettingsComponent)

    /**
     * Home component
     */
    override fun buildHomeComponent(): HomeComponent =
        HomeComponentImpl(this)

    override fun buildPlatformHomeComponent(homeComponent: HomeComponent): PlatformHomeComponent =
        PlatformHomeComponentImpl(homeComponent)

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

    override fun buildPlatformOnboardingComponent(onboardingComponent: OnboardingComponent): PlatformOnboardingComponent =
        PlatformOnboardingComponentImpl(onboardingComponent)

    /**
     * Placeholder new user component
     */
    override fun buildPlaceholderNewUserComponent(): PlaceholderNewUserComponent =
        PlaceholderNewUserComponentImpl(this)

    override fun buildPlatformPlaceholderNewUserComponent(placeholderNewUserComponent: PlaceholderNewUserComponent): PlatformPlaceholderNewUserComponent =
        PlatformPlaceholderNewUserComponentImpl(placeholderNewUserComponent)

    /**
     * Topics repetitions component
     */
    override fun buildTopicsRepetitionsComponent(): TopicsRepetitionsComponent =
        TopicsRepetitionsComponentImpl(this)

    override fun buildPlatformTopicsRepetitionsComponent(): PlatformTopicsRepetitionComponent =
        PlatformTopicsRepetitionComponentImpl(this)

    override fun buildTopicsRepetitionsDataComponent(): TopicsRepetitionsDataComponent =
        TopicsRepetitionsDataComponentImpl(this)

    /**
     * Debug component
     */
    override fun buildPlatformDebugComponent(debugComponent: DebugComponent): PlatformDebugComponent =
        PlatformDebugComponentImpl(debugComponent)

    override fun buildDebugComponent(): DebugComponent =
        DebugComponentImpl(this)

    /**
     * Topics to discover next component
     */
    override fun buildTopicsToDiscoverNextComponent(screen: TopicsToDiscoverNextScreen): TopicsToDiscoverNextComponent =
        TopicsToDiscoverNextComponentImpl(this, screen)

    override fun buildTopicsToDiscoverNextDataComponent(): TopicsToDiscoverNextDataComponent =
        TopicsToDiscoverNextDataComponentImpl(this)

    /**
     * ProblemsLimit component
     */
    override fun buildProblemsLimitComponent(): ProblemsLimitComponent =
        ProblemsLimitComponentImpl(this)

    override fun buildPlatformProblemsLimitComponent(): PlatformProblemsLimitComponent =
        PlatformProblemsLimitComponentImpl(problemsLimitComponent = buildProblemsLimitComponent())

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

    override fun buildGamificationToolbarComponent(): GamificationToolbarComponent =
        GamificationToolbarComponentImpl(this)

    override fun buildStudyPlanDataComponent(): StudyPlanDataComponent =
        StudyPlanDataComponentImpl(this)

    override fun buildProjectsDataComponent(): ProjectsDataComponent =
        ProjectsDataComponentImpl(this)

    override fun buildStagesDataComponent(): StagesDataComponent =
        StagesDataComponentImpl(this)

    override fun buildFreemiumDataComponent(): FreemiumDataComponent =
        FreemiumDataComponentImpl(this)
}