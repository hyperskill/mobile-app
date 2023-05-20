package org.hyperskill

import kotlin.time.DurationUnit
import kotlin.time.toDuration
import org.hyperskill.app.core.view.mapper.SharedDateFormatter

class DateFormatterTest {
    companion object {
        val ONE_SECOND_IN_MILLIS: Long = 1.toDuration(DurationUnit.SECONDS).inWholeMilliseconds
        val ONE_MINUTE_IN_MILLIS: Long = 1.toDuration(DurationUnit.MINUTES).inWholeMilliseconds
        val ONE_HOUR_IN_MILLIS: Long = 1.toDuration(DurationUnit.HOURS).inWholeMilliseconds
    }

    private val dateFormatter = SharedDateFormatter(ResourceProviderStub())

    // TODO: Implement tests

//    @Test
//    fun testMinutes() {
//        assertEquals(
//            "3 minutes",
//            dateFormatter.formatTimeDistance(3 * ONE_MINUTE_IN_MILLIS + 17 * 1000)
//        )
//
//        assertEquals(
//            "4 minutes",
//            dateFormatter.formatTimeDistance(3 * ONE_MINUTE_IN_MILLIS + 37 * 1000)
//        )
//
//        assertEquals(
//            "18 minutes",
//            dateFormatter.formatTimeDistance(
//                17 * ONE_MINUTE_IN_MILLIS + ONE_SECOND_IN_MILLIS * 30
//            )
//        )
//    }
//
//    @Test
//    fun testHours() {
//        assertEquals(
//            "about 1 hour",
//            dateFormatter.formatTimeDistance(49 * ONE_MINUTE_IN_MILLIS)
//        )
//
//        assertEquals(
//            "about 7 hours",
//            dateFormatter.formatTimeDistance(
//                7 * ONE_HOUR_IN_MILLIS + 17 * ONE_MINUTE_IN_MILLIS
//            )
//        )
//
//        assertEquals(
//            "about 24 hours",
//            dateFormatter.formatTimeDistance(
//                23 * ONE_HOUR_IN_MILLIS +
//                    59 * ONE_MINUTE_IN_MILLIS + ONE_SECOND_IN_MILLIS * 30
//            )
//        )
//    }
//
//    @Test
//    fun testMonths() {
//        assertEquals(
//            "about 1 month",
//            dateFormatter.formatTimeDistance(ONE_MINUTE_IN_MILLIS * 60 * 24 * 41)
//        )
//
//        assertEquals(
//            "about 2 months",
//            dateFormatter.formatTimeDistance(ONE_MINUTE_IN_MILLIS * 60 * 24 * 47)
//        )
//
//        assertEquals(
//            "3 months",
//            dateFormatter.formatTimeDistance(ONE_MINUTE_IN_MILLIS * 60 * 24 * 76)
//        )
//    }
//
//    @Test
//    fun testYears() {
//        assertEquals(
//            "about 1 year",
//            dateFormatter.formatTimeDistance(ONE_MINUTE_IN_MILLIS * 60 * 24 * 30 * 14)
//        )
//
//        assertEquals(
//            "about 2 years",
//            dateFormatter.formatTimeDistance(ONE_MINUTE_IN_MILLIS * 60 * 24 * 30 * 26)
//        )
//
//        assertEquals(
//            "over 2 years",
//            dateFormatter.formatTimeDistance(ONE_MINUTE_IN_MILLIS * 60 * 24 * 30 * 30)
//        )
//
//        assertEquals(
//            "almost 3 years",
//            dateFormatter.formatTimeDistance(ONE_MINUTE_IN_MILLIS * 60 * 24 * 30 * 34)
//        )
//    }
}