package org.hyperskill.sentry.transaction

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionTag

class HyperskillSentryTransactionTagTest {
    @Test
    fun `User is authorized tag creates correctly`() {
        val expectedKey = "user.is_authorized"

        val tagWithTrueValue = HyperskillSentryTransactionTag.User.IsAuthorized(isAuthorized = true)
        val tagWithFalseValue = HyperskillSentryTransactionTag.User.IsAuthorized(isAuthorized = false)

        assertEquals(expectedKey, tagWithTrueValue.key)
        assertEquals("true", tagWithTrueValue.value)

        assertEquals(expectedKey, tagWithFalseValue.key)
        assertEquals("false", tagWithFalseValue.value)
    }

    @Test
    fun `Study plan section is current tag creates correctly`() {
        val expectedKey = "study_plan.section.is_current"

        val tagWithTrueValue = HyperskillSentryTransactionTag.StudyPlan.Section.IsCurrent(isCurrent = true)
        val tagWithFalseValue = HyperskillSentryTransactionTag.StudyPlan.Section.IsCurrent(isCurrent = false)

        assertEquals(expectedKey, tagWithTrueValue.key)
        assertEquals("true", tagWithTrueValue.value)

        assertEquals(expectedKey, tagWithFalseValue.key)
        assertEquals("false", tagWithFalseValue.value)
    }
}