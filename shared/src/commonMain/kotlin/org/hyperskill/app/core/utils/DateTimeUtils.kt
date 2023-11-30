package org.hyperskill.app.core.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

val TimeZone.Companion.NYC: TimeZone
    get() = of("America/New_York")

object DateTimeUtils {
    /**
     * @return The number of seconds until the start of the next day in the "America/New_York" timezone.
     */
    fun secondsUntilStartOfNextDayInNewYork(): Long {
        val tzNewYork = TimeZone.NYC
        val nowInNewYork = Clock.System.now().toLocalDateTime(tzNewYork).toInstant(tzNewYork)
        val tomorrowInNewYork = nowInNewYork.plus(1, DateTimeUnit.DAY, tzNewYork).toLocalDateTime(tzNewYork)
        val startOfTomorrow = LocalDateTime(
            year = tomorrowInNewYork.year,
            month = tomorrowInNewYork.month,
            dayOfMonth = tomorrowInNewYork.dayOfMonth,
            hour = 0,
            minute = 0,
            second = 0,
            nanosecond = 0
        )
        return (startOfTomorrow.toInstant(tzNewYork) - nowInNewYork).inWholeSeconds
    }

    /**
     * @return The number of seconds until the start of the next Sunday in the "America/New_York" timezone.
     */
    fun secondsUntilNextSundayInNewYork(): Long {
        val tzNewYork = TimeZone.NYC
        val nowInNewYork = Clock.System.now().toLocalDateTime(tzNewYork)
        val nowInNewYorkInstant = nowInNewYork.toInstant(tzNewYork)

        // Calculate the number of days until the next Sunday and add that number of days to the current date.
        val nextSundayInNewYorkInstant = if (nowInNewYork.dayOfWeek == DayOfWeek.SUNDAY) {
            nowInNewYorkInstant.plus(1, DateTimeUnit.WEEK, tzNewYork)
        } else {
            val daysUntilSunday = (DayOfWeek.SUNDAY.ordinal - nowInNewYork.dayOfWeek.ordinal + 7) % 7
            nowInNewYorkInstant.plus(daysUntilSunday.toLong(), DateTimeUnit.DAY, tzNewYork)
        }

        val nextSundayInNewYork = nextSundayInNewYorkInstant.toLocalDateTime(tzNewYork)
        val startOfNextSunday = LocalDateTime(
            year = nextSundayInNewYork.year,
            month = nextSundayInNewYork.month,
            dayOfMonth = nextSundayInNewYork.dayOfMonth,
            hour = 0,
            minute = 0,
            second = 0,
            nanosecond = 0
        )

        return (startOfNextSunday.toInstant(tzNewYork) - nowInNewYorkInstant).inWholeSeconds
    }
}