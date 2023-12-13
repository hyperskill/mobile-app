package org.hyperskill.app.gamification_toolbar.domain.model

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransaction
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder

enum class GamificationToolbarScreen {
    HOME,
    STUDY_PLAN,
    LEADERBOARD;

    internal val analyticRoute: HyperskillAnalyticRoute
        get() = when (this) {
            HOME -> HyperskillAnalyticRoute.Home()
            STUDY_PLAN -> HyperskillAnalyticRoute.StudyPlan()
            LEADERBOARD -> HyperskillAnalyticRoute.Leaderboard()
        }

    internal val fetchContentSentryTransaction: HyperskillSentryTransaction
        get() = when (this) {
            HOME -> HyperskillSentryTransactionBuilder.buildGamificationToolbarHomeScreenRemoteDataLoading()
            STUDY_PLAN -> HyperskillSentryTransactionBuilder.buildGamificationToolbarStudyPlanScreenRemoteDataLoading()
            LEADERBOARD ->
                HyperskillSentryTransactionBuilder.buildGamificationToolbarLeaderboardScreenRemoteDataLoading()
        }
}