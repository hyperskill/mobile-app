package org.hyperskill

import org.hyperskill.app.extension.TimeFancifier
import kotlin.test.Test
import kotlin.test.assertEquals

class TimeFancifierTest {
    @Test
    fun testMinutes() {
        assertEquals(
            TimeFancifier.formatTimeDistance(3 * TimeFancifier.ONE_MINUTE_IN_MILLIS + 17 * 1000),
            "1 minutes"
        )

        assertEquals(
            TimeFancifier.formatTimeDistance(3 * TimeFancifier.ONE_MINUTE_IN_MILLIS + 37 * 1000),
            "4 minutes"
        )

        assertEquals(
            TimeFancifier.formatTimeDistance(17 * TimeFancifier.ONE_MINUTE_IN_MILLIS + TimeFancifier.THIRTY_SEC_IN_MILLIS),
            "18 minutes"
        )
    }

    @Test
    fun testHours() {
        assertEquals(
            TimeFancifier.formatTimeDistance(49 * TimeFancifier.ONE_MINUTE_IN_MILLIS),
            "about 1 hour"
        )

        assertEquals(
            TimeFancifier.formatTimeDistance(7 * TimeFancifier.ONE_HOUR_IN_MILLIS + 17 * TimeFancifier.ONE_MINUTE_IN_MILLIS),
            "about 7 hours"
        )

        assertEquals(
            TimeFancifier.formatTimeDistance(23 * TimeFancifier.ONE_HOUR_IN_MILLIS + 59 * TimeFancifier.ONE_MINUTE_IN_MILLIS + TimeFancifier.THIRTY_SEC_IN_MILLIS),
            "about 24 hours"
        )
    }

    @Test
    fun testMonths() {
        assertEquals(
            TimeFancifier.formatTimeDistance(TimeFancifier.ONE_MINUTE_IN_MILLIS * 60 * 24 * 41),
            "about 1 month"
        )

        assertEquals(
            TimeFancifier.formatTimeDistance(TimeFancifier.ONE_MINUTE_IN_MILLIS * 60 * 24 * 47),
            "about 2 months"
        )

        assertEquals(
            TimeFancifier.formatTimeDistance(TimeFancifier.ONE_MINUTE_IN_MILLIS * 60 * 24 * 76),
            "3 months"
        )
    }

    @Test
    fun testYears() {
        assertEquals(
            TimeFancifier.formatTimeDistance(TimeFancifier.ONE_MINUTE_IN_MILLIS * 60 * 24 * 30 * 14),
            "about 1 year"
        )

        assertEquals(
            TimeFancifier.formatTimeDistance(TimeFancifier.ONE_MINUTE_IN_MILLIS * 60 * 24 * 30 * 26),
            "about 2 years"
        )

        assertEquals(
            TimeFancifier.formatTimeDistance(TimeFancifier.ONE_MINUTE_IN_MILLIS * 60 * 24 * 30 * 30),
            "over 2 years"
        )

        assertEquals(
            TimeFancifier.formatTimeDistance(TimeFancifier.ONE_MINUTE_IN_MILLIS * 60 * 24 * 30 * 34),
            "almost 3 years"
        )
    }
}