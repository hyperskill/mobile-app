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
    fun buildAppScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "app-feature-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
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
     * NavigationBarItemsFeature
     */
    fun buildNavigationBarItemsHomeScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "navigation-bar-items-feature-home-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )

    fun buildNavigationBarItemsTrackScreenRemoteDataLoading(): HyperskillSentryTransaction =
        HyperskillSentryTransaction(
            name = "navigation-bar-items-feature-track-screen-remote-data-loading",
            operation = HyperskillSentryTransactionOperation.API_LOAD
        )
}