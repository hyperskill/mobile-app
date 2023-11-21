package org.hyperskill.core.view.mapper.date

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.Month
import org.hyperskill.app.core.view.mapper.date.MonthFormatter

class MonthFormatterTest {
    @Test
    fun `Format Month to short format correct`() {
        val months = listOf(
            Month.JANUARY,
            Month.FEBRUARY,
            Month.MARCH,
            Month.APRIL,
            Month.MAY,
            Month.JUNE,
            Month.JULY,
            Month.AUGUST,
            Month.SEPTEMBER,
            Month.OCTOBER,
            Month.NOVEMBER,
            Month.DECEMBER
        )
        val expected = listOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        )
        for (i in months.indices) {
            val month = months[i]
            val actual = MonthFormatter.formatMonthToShort(month)
            assertEquals(expected[i], actual)
        }
    }
}