package org.hyperskill.app.core.view.mapper.date

import kotlin.math.max
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider

class SharedDateFormatter(private val resourceProvider: ResourceProvider) {
    companion object {
        private val DURATION_THIRTY_SECONDS = 30.toDuration(DurationUnit.SECONDS)

        private val DURATION_ONE_MINUTE = 1.toDuration(DurationUnit.MINUTES)
        private val DURATION_FORTY_FIVE_MINUTES = 45.toDuration(DurationUnit.MINUTES)

        private val DURATION_ONE_HOUR = 1.toDuration(DurationUnit.HOURS)

        private val DURATION_ONE_DAY = 1.toDuration(DurationUnit.DAYS)

        private const val DAYS_IN_MONTH = 30
        private val DURATION_ONE_MONTH = DAYS_IN_MONTH.toDuration(DurationUnit.DAYS)

        private const val DAYS_IN_YEAR = 365
        private const val MONTHS_IN_YEAR = 12
        private val DURATION_ONE_YEAR = DAYS_IN_YEAR.toDuration(DurationUnit.DAYS)

        private const val HOURS_IN_DAY = 24
        private const val MINUTES_IN_HOUR = 60
    }

    fun formatTimeDistance(instant: Instant): String =
        formatTimeDistance(Clock.System.now().toEpochMilliseconds() - instant.toEpochMilliseconds())

    fun formatTimeDistance(millis: Long): String {
        val duration = millis.toDuration(DurationUnit.MILLISECONDS)

        if (duration <= DURATION_THIRTY_SECONDS) {
            return resourceProvider.getString(SharedResources.strings.date_formatter_less_than_a_minute)
        }

        if (duration < DURATION_FORTY_FIVE_MINUTES) {
            val minutes = duration.inWholeMinutes.toInt()
            return resourceProvider.getQuantityString(SharedResources.plurals.minutes, minutes, minutes)
        }

        if (duration < DURATION_ONE_DAY) {
            val hours = duration.inWholeHours.toInt()
            return resourceProvider.getQuantityString(SharedResources.plurals.about_hours, hours, hours)
        }

        val days = duration.inWholeDays.toInt()

        if (duration < DURATION_ONE_MONTH) {
            return resourceProvider.getQuantityString(SharedResources.plurals.days, days, days)
        }

        val months = days / DAYS_IN_MONTH

        if (duration < DURATION_ONE_MONTH * 2) {
            return resourceProvider.getQuantityString(SharedResources.plurals.about_months, months, months)
        }

        if (duration < DURATION_ONE_YEAR) {
            return resourceProvider.getQuantityString(SharedResources.plurals.months, months, months)
        }

        val monthsSinceStartOfYear = months % MONTHS_IN_YEAR
        val years = months.floorDiv(MONTHS_IN_YEAR)

        return resourceProvider.getQuantityString(
            if (monthsSinceStartOfYear < 3) {
                SharedResources.plurals.about_years
            } else if (monthsSinceStartOfYear < 9) {
                SharedResources.plurals.over_years
            } else {
                SharedResources.plurals.almost_years
            },
            years,
            years
        )
    }

    /**
     * Format days, hours and minutes count with localized and pluralized suffix;
     * 86400 -> "1 day", 86460 -> "1 day 1 minute", 90000 -> "1 day 1 hour", 59 -> "1 minute"
     *
     * @param seconds Seconds to format
     * @return formatted days, hours and minutes count
     */
    fun formatDaysWithHoursAndMinutesCount(seconds: Long): String {
        val duration = seconds.toDuration(DurationUnit.SECONDS)

        val days = duration.inWholeDays.toInt()
        val hours = duration.inWholeHours.toInt() % HOURS_IN_DAY
        val minutes = duration.inWholeMinutes.toInt() % MINUTES_IN_HOUR

        if (days == 0 && hours == 0 && minutes == 0) {
            return resourceProvider.getQuantityString(SharedResources.plurals.minutes, 1, 1)
        }

        return buildString {
            if (days > 0) {
                append("${resourceProvider.getQuantityString(SharedResources.plurals.days, days, days)} ")
            }
            if (hours > 0) {
                append("${resourceProvider.getQuantityString(SharedResources.plurals.hours, hours, hours)} ")
            }
            if (minutes > 0) {
                append(resourceProvider.getQuantityString(SharedResources.plurals.minutes, minutes, minutes))
            }
        }
    }

    /**
     * Format hours and minutes count with localized and pluralized suffix;
     * 7260 -> "2 hours 1 minute", 7320 -> "2 hours 2 minute", 21600 -> "6 hours"
     * @param seconds Seconds to format
     */
    fun formatHoursWithMinutesCount(seconds: Long): String {
        val duration = seconds.toDuration(DurationUnit.SECONDS)
        val hours = duration.inWholeHours.toInt()
        val minutes = duration.inWholeMinutes.toInt() % 60

        var result = ""

        if (hours > 0) {
            result += resourceProvider.getQuantityString(SharedResources.plurals.hours, hours, hours)
            if (minutes > 0) {
                result += " ${resourceProvider.getQuantityString(SharedResources.plurals.minutes, minutes, minutes)}"
            }
        } else {
            val positiveMinutes = max(1, minutes)
            result += resourceProvider.getQuantityString(
                SharedResources.plurals.minutes,
                positiveMinutes,
                positiveMinutes
            )
        }

        return result
    }

    /**
     * Format hours or minutes count with localized and pluralized suffix;
     * 7390.0 -> "2 hours", 776.0 -> "12 minutes"
     * @param seconds seconds to format
     *
     */
    fun formatHoursOrMinutesCount(seconds: Float?): String? =
        if (seconds == null || seconds <= 0) {
            null
        } else {
            val duration = seconds.toInt().toDuration(DurationUnit.SECONDS)
            formatHoursOrMinutesCount(duration)
        }

    /**
     * Format hours or minutes count with localized and pluralized suffix;
     * 02:03:10 -> "2 hours", 00:12:56 -> "12 minutes"
     * @param duration Duration to format
     *
     */
    fun formatHoursOrMinutesCount(duration: Duration): String =
        if (duration > DURATION_ONE_HOUR) {
            val hours = duration.inWholeHours.toInt()
            resourceProvider.getQuantityString(SharedResources.plurals.hours, hours, hours)
        } else {
            val minutes = duration.inWholeMinutes.toInt()
            resourceProvider.getQuantityString(SharedResources.plurals.minutes, minutes, minutes)
        }

    /**
     * Format hours or minutes count with localized and pluralized suffix;
     * 02:03:10 -> "2 h", 00:12:56 -> "12 m"
     * @param duration Duration to format
     *
     */
    fun formatHoursOrMinutesCountShort(duration: Duration): String =
        if (duration > DURATION_ONE_HOUR) {
            val hours = duration.inWholeHours.toInt()
            resourceProvider.getString(SharedResources.strings.hours_short, hours)
        } else {
            val minutes = duration.inWholeMinutes.toInt()
            resourceProvider.getString(SharedResources.strings.minutes_short, minutes)
        }

    /**
     * Format hours count (with floor rounding) with localized and pluralized suffix;
     * 7260 -> "2 hours", 7320 -> "2 hours", 21600 -> "6 hours", 3599 -> null, null -> null
     * @param seconds Seconds to format
     *
     */
    fun formatHoursCount(seconds: Float?): String? {
        if (seconds == null || seconds <= 0) return null

        val hours = seconds.toInt().toDuration(DurationUnit.SECONDS).inWholeHours.toInt()

        return if (hours >= 1) {
            resourceProvider.getQuantityString(SharedResources.plurals.hours, hours, hours)
        } else {
            null
        }
    }

    /**
     * Format minutes or seconds count with localized and pluralized suffix;
     * 42 -> "42 seconds", 123 -> "2 minutes"
     * @param secondsToFormat Seconds to format
     *
     */
    fun formatMinutesOrSecondsCount(secondsToFormat: Float): String {
        val duration = secondsToFormat.toInt().toDuration(DurationUnit.SECONDS)

        return if (duration > DURATION_ONE_MINUTE) {
            val minutes = duration.inWholeMinutes.toInt()
            resourceProvider.getQuantityString(SharedResources.plurals.minutes, minutes, minutes)
        } else {
            val seconds = duration.inWholeSeconds.toInt()
            resourceProvider.getQuantityString(SharedResources.plurals.seconds, seconds, seconds)
        }
    }

    /**
     * Format month and day of local date;
     *
     * 2023-11-02 -> "2 Nov"
     *
     * @param localDate local date to format
     * @return formatted month and day
     */
    fun formatDayNumericAndMonthShort(localDate: LocalDate): String =
        "${localDate.dayOfMonth} ${MonthFormatter.formatMonthToShort(localDate.month)}"

    fun formatSubscriptionValidUntil(instant: Instant, timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
        val localDateTime = instant.toLocalDateTime(timeZone)
        val month = MonthFormatter.formatMonth(localDateTime.month)
        val dayOfMonth = localDateTime.date.dayOfMonth
        val hour = formatHoursOrMinutesWithLeadingZero(localDateTime.hour)
        val minutes = formatHoursOrMinutesWithLeadingZero(localDateTime.minute)
        return "$month $dayOfMonth, ${localDateTime.year}, $hour:$minutes"
    }

    private fun formatHoursOrMinutesWithLeadingZero(count: Int): String =
        if (count <= 9) "0$count" else count.toString()
}