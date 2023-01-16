package org.hyperskill

import org.hyperskill.app.core.view.mapper.DateFormatter
import kotlin.test.Test
import kotlin.test.assertEquals

class DateFormatterTest {
    @Test
    fun testMinutes() {
        assertEquals(
            DateFormatter.formatTimeDistance(3 * DateFormatter.ONE_MINUTE_IN_MILLIS + 17 * 1000),
            "3 minutes"
        )

        assertEquals(
            DateFormatter.formatTimeDistance(3 * DateFormatter.ONE_MINUTE_IN_MILLIS + 37 * 1000),
            "4 minutes"
        )

        assertEquals(
            DateFormatter.formatTimeDistance(17 * DateFormatter.ONE_MINUTE_IN_MILLIS + DateFormatter.THIRTY_SEC_IN_MILLIS),
            "18 minutes"
        )
    }

    @Test
    fun testHours() {
        assertEquals(
            DateFormatter.formatTimeDistance(49 * DateFormatter.ONE_MINUTE_IN_MILLIS),
            "about 1 hour"
        )

        assertEquals(
            DateFormatter.formatTimeDistance(7 * DateFormatter.ONE_HOUR_IN_MILLIS + 17 * DateFormatter.ONE_MINUTE_IN_MILLIS),
            "about 7 hours"
        )

        assertEquals(
            DateFormatter.formatTimeDistance(23 * DateFormatter.ONE_HOUR_IN_MILLIS + 59 * DateFormatter.ONE_MINUTE_IN_MILLIS + DateFormatter.THIRTY_SEC_IN_MILLIS),
            "about 24 hours"
        )
    }

    @Test
    fun testMonths() {
        assertEquals(
            DateFormatter.formatTimeDistance(DateFormatter.ONE_MINUTE_IN_MILLIS * 60 * 24 * 41),
            "about 1 month"
        )

        assertEquals(
            DateFormatter.formatTimeDistance(DateFormatter.ONE_MINUTE_IN_MILLIS * 60 * 24 * 47),
            "about 2 months"
        )

        assertEquals(
            DateFormatter.formatTimeDistance(DateFormatter.ONE_MINUTE_IN_MILLIS * 60 * 24 * 76),
            "3 months"
        )
    }

    @Test
    fun testYears() {
        assertEquals(
            DateFormatter.formatTimeDistance(DateFormatter.ONE_MINUTE_IN_MILLIS * 60 * 24 * 30 * 14),
            "about 1 year"
        )

        assertEquals(
            DateFormatter.formatTimeDistance(DateFormatter.ONE_MINUTE_IN_MILLIS * 60 * 24 * 30 * 26),
            "about 2 years"
        )

        assertEquals(
            DateFormatter.formatTimeDistance(DateFormatter.ONE_MINUTE_IN_MILLIS * 60 * 24 * 30 * 30),
            "over 2 years"
        )

        assertEquals(
            DateFormatter.formatTimeDistance(DateFormatter.ONE_MINUTE_IN_MILLIS * 60 * 24 * 30 * 34),
            "almost 3 years"
        )
    }
}