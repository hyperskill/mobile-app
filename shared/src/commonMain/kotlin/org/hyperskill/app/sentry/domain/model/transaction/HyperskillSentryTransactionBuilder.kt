package org.hyperskill.app.sentry.domain.model.transaction

/**
 * Please do not change the names of transactions if they already appeared in the Sentry.
 */
object HyperskillSentryTransactionBuilder {
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
     * PlaceholderNewUserFeature
     */
    fun buildPlaceholderNewUserScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "placeholder-new-user-feature-screen-remote-data-loading",
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
     * StepFeature
     */
    fun buildStepScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "step-feature-screen-remote-data-loading",
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

    /**
     * StepQuizFeature
     */
    fun buildStepQuizScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "step-quiz-feature-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildStepQuizCreateAttempt(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "step-quiz-feature-create-attempt",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildStepQuizCreateSubmission(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "step-quiz-feature-create-submission",
            operation = HyperskillSentryTransactionOperation.API_LOAD
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
     * TrackFeature
     */
    fun buildTrackScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "track-feature-screen-remote-data-loading",
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

    fun buildGamificationToolbarTrackScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "navigation-bar-items-feature-track-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildGamificationToolbarStudyPlanScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "navigation-bar-items-feature-study_plan-screen-remote-data-loading",
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

    fun buildProblemsLimitStepQuizScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "problems-limit-feature-step-quiz-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * TopicsToDiscoverNextFeature
     */
    fun buildTopicsToDiscoverNextHomeScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "topics-to-discover-next-feature-home-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildTopicsToDiscoverNextTrackScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "topics-to-discover-next-feature-track-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    /**
     * StudyPlanWidgetFeature
     */
    fun buildStudyPlanWidgetFetchCurrentStudyPlan(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "study-plan-widget-feature-fetch-current-study-plan",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildStudyPlanWidgetFetchStudyPlanSections(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "study-plan-widget-feature-fetch-study-plan-sections",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildStudyPlanWidgetFetchLearningActivities(isCurrentSection: Boolean): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "study-plan-widget-feature-fetch-learning-activities",
            operation = HyperskillSentryTransactionOperation.API_LOAD,
            tags = listOf(
                HyperskillSentryTransactionTag.StudyPlan.Section.IsCurrent(isCurrentSection)
            )
        )

    fun buildStudyPlanWidgetFetchTrack(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "study-plan-widget-feature-fetch-track",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )
}