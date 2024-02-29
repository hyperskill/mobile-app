package org.hyperskill.app.core.view.mapper.date

import kotlinx.datetime.Month

internal object MonthFormatter {
    /**
     * Format month to 3 letters abbreviation
     *
     * @param month month to format
     * @return 3 letters abbreviation of month
     */
    fun formatMonthToShort(month: Month): String =
        when (month) {
            Month.JANUARY -> "Jan"
            Month.FEBRUARY -> "Feb"
            Month.MARCH -> "Mar"
            Month.APRIL -> "Apr"
            Month.MAY -> "May"
            Month.JUNE -> "Jun"
            Month.JULY -> "Jul"
            Month.AUGUST -> "Aug"
            Month.SEPTEMBER -> "Sep"
            Month.OCTOBER -> "Oct"
            Month.NOVEMBER -> "Nov"
            Month.DECEMBER -> "Dec"
            else -> throw IllegalArgumentException("MonthFormatter: unknown month $month")
        }

    fun formatMonth(month: Month): String =
        when (month) {
            Month.JANUARY -> "January"
            Month.FEBRUARY -> "February"
            Month.MARCH -> "March"
            Month.APRIL -> "April"
            Month.MAY -> "May"
            Month.JUNE -> "June"
            Month.JULY -> "July"
            Month.AUGUST -> "August"
            Month.SEPTEMBER -> "September"
            Month.OCTOBER -> "October"
            Month.NOVEMBER -> "November"
            Month.DECEMBER -> "December"
            else -> throw IllegalArgumentException("MonthFormatter: unknown month $month")
        }
}