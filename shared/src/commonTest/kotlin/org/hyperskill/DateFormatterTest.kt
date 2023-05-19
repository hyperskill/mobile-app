package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.core.view.mapper.SharedDateFormatter

class DateFormatterTest {
    @Test
    fun testMinutes() {
        assertEquals(
            SharedDateFormatter.formatTimeDistance(3 * SharedDateFormatter.ONE_MINUTE_IN_MILLIS + 17 * 1000),
            "3 minutes"
        )

        assertEquals(
            SharedDateFormatter.formatTimeDistance(3 * SharedDateFormatter.ONE_MINUTE_IN_MILLIS + 37 * 1000),
            "4 minutes"
        )

        assertEquals(
            SharedDateFormatter.formatTimeDistance(
                17 * SharedDateFormatter.ONE_MINUTE_IN_MILLIS + SharedDateFormatter.THIRTY_SEC_IN_MILLIS
            ),
            "18 minutes"
        )
    }

    @Test
    fun testHours() {
        assertEquals(
            SharedDateFormatter.formatTimeDistance(49 * SharedDateFormatter.ONE_MINUTE_IN_MILLIS),
            "about 1 hour"
        )

        assertEquals(
            SharedDateFormatter.formatTimeDistance(
                7 * SharedDateFormatter.ONE_HOUR_IN_MILLIS + 17 * SharedDateFormatter.ONE_MINUTE_IN_MILLIS
            ),
            "about 7 hours"
        )

        assertEquals(
            SharedDateFormatter.formatTimeDistance(
                23 * SharedDateFormatter.ONE_HOUR_IN_MILLIS +
                    59 * SharedDateFormatter.ONE_MINUTE_IN_MILLIS + SharedDateFormatter.THIRTY_SEC_IN_MILLIS
            ),
            "about 24 hours"
        )
    }

    @Test
    fun testMonths() {
        assertEquals(
            SharedDateFormatter.formatTimeDistance(SharedDateFormatter.ONE_MINUTE_IN_MILLIS * 60 * 24 * 41),
            "about 1 month"
        )

        assertEquals(
            SharedDateFormatter.formatTimeDistance(SharedDateFormatter.ONE_MINUTE_IN_MILLIS * 60 * 24 * 47),
            "about 2 months"
        )

        assertEquals(
            SharedDateFormatter.formatTimeDistance(SharedDateFormatter.ONE_MINUTE_IN_MILLIS * 60 * 24 * 76),
            "3 months"
        )
    }

    @Test
    fun testYears() {
        assertEquals(
            SharedDateFormatter.formatTimeDistance(SharedDateFormatter.ONE_MINUTE_IN_MILLIS * 60 * 24 * 30 * 14),
            "about 1 year"
        )

        assertEquals(
            SharedDateFormatter.formatTimeDistance(SharedDateFormatter.ONE_MINUTE_IN_MILLIS * 60 * 24 * 30 * 26),
            "about 2 years"
        )

        assertEquals(
            SharedDateFormatter.formatTimeDistance(SharedDateFormatter.ONE_MINUTE_IN_MILLIS * 60 * 24 * 30 * 30),
            "over 2 years"
        )

        assertEquals(
            SharedDateFormatter.formatTimeDistance(SharedDateFormatter.ONE_MINUTE_IN_MILLIS * 60 * 24 * 30 * 34),
            "almost 3 years"
        )
    }
}