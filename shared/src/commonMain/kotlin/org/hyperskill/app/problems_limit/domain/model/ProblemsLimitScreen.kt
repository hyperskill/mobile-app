package org.hyperskill.app.problems_limit.domain.model

import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransaction
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder

enum class ProblemsLimitScreen {
    HOME,
    STEP_QUIZ;

    internal val sentryTransaction: HyperskillSentryTransaction
        get() = when (this) {
            HOME -> HyperskillSentryTransactionBuilder.buildProblemsLimitHomeScreenRemoteDataLoading()
            STEP_QUIZ -> HyperskillSentryTransactionBuilder.buildProblemsLimitStepQuizScreenRemoteDataLoading()
        }
}