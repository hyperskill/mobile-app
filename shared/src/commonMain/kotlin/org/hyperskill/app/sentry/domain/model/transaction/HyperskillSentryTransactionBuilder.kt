package org.hyperskill.app.sentry.domain.model.transaction

/**
 * Please do not change the names of transactions if they already appeared in the Sentry.
 */
object HyperskillSentryTransactionBuilder {
    fun buildAppInitialization(isAuthorized: Boolean): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "app-initialization",
            operation = HyperskillSentryTransactionOperation.UI_LOAD,
            tags = listOf(
                HyperskillSentryTransactionTag.User.IsAuthorized(isAuthorized)
            )
        )

    /**
     * AuthCredentialsFeature
     */
    fun buildAuthCredentialsAuth(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "auth-credentials-feature-auth-with-email",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * AuthSocialFeature
     */
    fun buildAuthSocialAuth(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "auth-social-feature-auth-with-social",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * AppFeature
     */
    fun buildAppScreenRemoteDataLoading(isAuthorized: Boolean): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "app-feature-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD,
            tags = listOf(
                HyperskillSentryTransactionTag.User.IsAuthorized(isAuthorized)
            )
        )

    /**
     * HomeFeature
     */
    fun buildHomeScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "home-feature-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * ProfileFeature
     */
    fun buildProfileScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "profile-feature-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * LeaderboardWidgetFeature
     */
    fun buildLeaderboardWidgetRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "leaderboard-widget-feature-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * StepFeature
     */
    fun buildStepScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "step-feature-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildSkipStep(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "step-feature-skip-step-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildSkipStepNextLearningActivityLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "step-feature-skip-step-next-step-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * StepCompletionFeature
     */
    fun buildStepCompletionNextStepLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "step-completion-feature-next-step-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildCheckTopicCompletionStatusLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "step-completion-feature-check-topic-completion-status",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * StepQuizFeature
     */
    fun buildStepQuizScreenRemoteDataLoading(blockName: String): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "step-quiz-feature-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD,
            tags = listOf(
                HyperskillSentryTransactionTag.Step.Block.Name(blockName)
            )
        )

    fun buildStepQuizCreateAttempt(blockName: String): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "step-quiz-feature-create-attempt",
            operation = HyperskillSentryTransactionOperation.API_LOAD,
            tags = listOf(
                HyperskillSentryTransactionTag.Step.Block.Name(blockName)
            )
        )

    fun buildStepQuizCreateSubmission(blockName: String): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "step-quiz-feature-create-submission",
            operation = HyperskillSentryTransactionOperation.API_LOAD,
            tags = listOf(
                HyperskillSentryTransactionTag.Step.Block.Name(blockName)
            )
        )

    /**
     * StepQuizHintsFeature
     */
    fun buildStepQuizHintsScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "step-quiz-hints-feature-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildStepQuizHintsReportHint(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "step-quiz-hints-feature-report-hint",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildStepQuizHintsReactHint(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "step-quiz-hints-feature-react-hint",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildStepQuizHintsFetchNextHint(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "step-quiz-hints-feature-fetch-next-hint",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * StageImplementFeature
     */
    fun buildStageImplementScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "stage-implement-feature-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * TopicsRepetitionsFeature
     */
    fun buildTopicsRepetitionsScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "topics-repetitions-feature-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildTopicsRepetitionsFetchNextTopics(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "topics-repetitions-feature-fetch-next-topics",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * GamificationToolbarFeature
     */
    fun buildGamificationToolbarHomeScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "navigation-bar-items-feature-home-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildGamificationToolbarStudyPlanScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "navigation-bar-items-feature-study_plan-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildGamificationToolbarLeaderboardScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "navigation-bar-items-feature-leaderboard-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * ProblemsLimitFeature
     */
    fun buildProblemsLimitHomeScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "problems-limit-feature-home-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildProblemsLimitStudyPlanScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "problems-limit-feature-study-plan-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * StudyPlanWidgetFeature
     */
    fun buildStudyPlanWidgetFetchLearningActivities(isCurrentSection: Boolean): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "study-plan-widget-feature-fetch-learning-activities",
            operation = HyperskillSentryTransactionOperation.API_LOAD,
            tags = listOf(
                HyperskillSentryTransactionTag.StudyPlan.Section.IsCurrent(isCurrentSection)
            )
        )

    fun buildStudyPlanWidgetFetchLearningActivitiesWithSections(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "study-plan-widget-feature-fetch-learning-activities-with-sections",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildStudyPlanWidgetFetchProfile(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "study-plan-widget-feature-fetch-profile",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * ProjectSelectionListFeature
     */
    fun buildProjectSelectionListScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "project-selection-list-feature-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * ProjectSelectionDetailsFeature
     */
    fun buildProjectSelectionDetailsScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "project-selection-details-feature-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * TrackSelectionListFeature
     */
    fun buildTrackSelectionListScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "track-selection-list-feature-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * TrackSelectionDetailsFeature
     */
    fun buildTrackSelectionDetailsScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "track-selection-details-feature-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildTrackSelectionDetailsScreenSelectTrack(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "track-selection-details-feature-select-track",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * ProgressScreenFeature
     */
    fun buildProgressScreenRemoteTrackWithProgressLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "progress-screen-remote-track-with-progress-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildProgressScreenRemoteProjectWithProgressLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "progress-screen-remote-project-with-progress-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * FirstProblemOnboardingFeature
     */
    fun buildFirstProblemOnboardingFeatureProfileDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "first-problem-onboarding-feature-profile-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildFirstProblemOnboardingFeatureFetchNextLearningActivity(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "first-problem-onboarding-feature-fetch-next-learning-activity",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildWelcomeOnboardingFeatureFetchNextLearningActivity(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "welcome-onboarding-feature-fetch-next-learning-activity",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * ChallengeWidgetFeature
     */
    fun buildChallengeWidgetFeatureFetchChallenges(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "challenge-widget-feature-fetch-challenges",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * SearchFeature
     */
    fun buildSearchFeaturePerformSearch(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "search-feature-perform-search",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * StreakRecoveryFeature
     */
    fun buildStreakRecoveryFeatureFetchStreak(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "streak-recovery-feature-fetch-streak",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * ProfileSettingsFeature
     */
    fun buildProfileSettingsFeatureFetchSubscription(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "profile-settings-feature-fetch-subscription",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildManageSubscriptionFeatureFetchSubscription(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "manage-subscription-feature-fetch-subscription",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * PaywallFeature
     */
    fun buildPaywallFeatureSyncSubscription(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "paywall-feature-sync-subscription",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildPaywallFeaturePurchaseSubscription(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "paywall-feature-purchase-subscription",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildPaywallFetchSubscriptionPrice(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "paywall-feature-fetch-subscription-price",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * CommentsScreenFeature
     */
    fun buildCommentsScreenFeatureFetchDiscussions(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "comments-screen-feature-fetch-discussions",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildCommentsScreenFeatureFetchDiscussionReplies(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "comments-screen-feature-fetch-discussion-replies",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )
}