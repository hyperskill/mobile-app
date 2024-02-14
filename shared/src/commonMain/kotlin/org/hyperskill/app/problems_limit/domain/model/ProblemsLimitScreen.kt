package org.hyperskill.app.problems_limit.domain.model

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransaction
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder

enum class ProblemsLimitScreen {
    HOME,
    STUDY_PLAN;

    internal val sentryTransaction: HyperskillSentryTransaction
        get() = when (this) {
            HOME -> HyperskillSentryTransactionBuilder.buildProblemsLimitHomeScreenRemoteDataLoading()
            STUDY_PLAN -> HyperskillSentryTransactionBuilder.buildProblemsLimitStudyPlanScreenRemoteDataLoading()
        }

    internal val analyticRoute: HyperskillAnalyticRoute
        get() = when (this) {
            HOME -> HyperskillAnalyticRoute.Home()
            STUDY_PLAN -> HyperskillAnalyticRoute.StudyPlan()
        }
}