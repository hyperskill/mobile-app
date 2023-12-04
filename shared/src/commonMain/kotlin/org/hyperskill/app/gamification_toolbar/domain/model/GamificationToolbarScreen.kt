package org.hyperskill.app.gamification_toolbar.domain.model

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransaction
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder

enum class GamificationToolbarScreen {
    HOME,
    TRACK,
    STUDY_PLAN,
    LEADERBOARD;

    internal val analyticRoute: HyperskillAnalyticRoute
        get() = when (this) {
            HOME -> HyperskillAnalyticRoute.Home()
            TRACK -> HyperskillAnalyticRoute.Track()
            STUDY_PLAN -> HyperskillAnalyticRoute.StudyPlan()
            LEADERBOARD -> HyperskillAnalyticRoute.Leaderboard()
        }

    internal val fetchContentSentryTransaction: HyperskillSentryTransaction
        get() = when (this) {
            HOME -> HyperskillSentryTransactionBuilder.buildGamificationToolbarHomeScreenRemoteDataLoading()
            TRACK -> HyperskillSentryTransactionBuilder.buildGamificationToolbarTrackScreenRemoteDataLoading()
            STUDY_PLAN -> HyperskillSentryTransactionBuilder.buildGamificationToolbarStudyPlanScreenRemoteDataLoading()
            LEADERBOARD ->
                HyperskillSentryTransactionBuilder.buildGamificationToolbarLeaderboardScreenRemoteDataLoading()
        }
}