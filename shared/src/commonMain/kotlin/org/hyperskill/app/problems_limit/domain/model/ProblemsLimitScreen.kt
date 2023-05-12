package org.hyperskill.app.problems_limit.domain.model

import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransaction
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder

enum class ProblemsLimitScreen {
    HOME,
    STUDY_PLAN,
    STEP_QUIZ;

    internal val sentryTransaction: HyperskillSentryTransaction
        get() = when (this) {
            HOME -> HyperskillSentryTransactionBuilder.buildProblemsLimitHomeScreenRemoteDataLoading()
            STUDY_PLAN -> HyperskillSentryTransactionBuilder.buildProblemsLimitStudyPlanScreenRemoteDataLoading()
            STEP_QUIZ -> HyperskillSentryTransactionBuilder.buildProblemsLimitStepQuizScreenRemoteDataLoading()
        }
}