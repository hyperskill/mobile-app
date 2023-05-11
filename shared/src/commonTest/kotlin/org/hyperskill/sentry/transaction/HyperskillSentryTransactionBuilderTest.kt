package org.hyperskill.sentry.transaction

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionOperation
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionTag

class HyperskillSentryTransactionBuilderTest {
    @Test
    fun `AppScreenRemoteDataLoading transaction builds correctly`() {
        val expectedName = "app-feature-remote-data-loading"
        val expectedOperation = HyperskillSentryTransactionOperation.API_LOAD.stringValue
        val expectedTag = HyperskillSentryTransactionTag.User.IsAuthorized(isAuthorized = true)

        val transaction = HyperskillSentryTransactionBuilder.buildAppScreenRemoteDataLoading(isAuthorized = true)

        assertEquals(expectedName, transaction.name)
        assertEquals(expectedOperation, transaction.operation)

        val actualTag = transaction.tags.toList().first()
        assertEquals(expectedTag.key, actualTag.first)
        assertEquals(expectedTag.value, actualTag.second)
    }

    @Test
    fun `StudyPlanWidgetFetchLearningActivities transaction builds correctly`() {
        val expectedName = "study-plan-widget-feature-fetch-learning-activities"
        val expectedOperation = HyperskillSentryTransactionOperation.API_LOAD.stringValue
        val expectedTag = HyperskillSentryTransactionTag.StudyPlan.Section.IsCurrent(isCurrent = true)

        val transaction =
            HyperskillSentryTransactionBuilder.buildStudyPlanWidgetFetchLearningActivities(isCurrentSection = true)

        assertEquals(expectedName, transaction.name)
        assertEquals(expectedOperation, transaction.operation)

        val actualTag = transaction.tags.toList().first()
        assertEquals(expectedTag.key, actualTag.first)
        assertEquals(expectedTag.value, actualTag.second)
    }
}