package org.hyperskill.app.core.view.mapper

import kotlin.math.floor
import kotlin.math.max
import kotlin.math.roundToLong
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import org.hyperskill.app.SharedResources

class SharedDateFormatter(private val resourceProvider: ResourceProvider) {
    companion object {
        private val DURATION_ONE_HOUR = 1.toDuration(DurationUnit.HOURS)
        private val DURATION_ONE_MINUTE = 1.toDuration(DurationUnit.MINUTES)

        private val SECONDS_PER_HOUR = DURATION_ONE_HOUR.inWholeSeconds
        private val SECONDS_PER_MINUTE = DURATION_ONE_MINUTE.inWholeSeconds

        val THIRTY_SEC_IN_MILLIS: Long = 30.toDuration(DurationUnit.SECONDS).inWholeMilliseconds
        val ONE_MINUTE_IN_MILLIS: Long = 1.toDuration(DurationUnit.MINUTES).inWholeMilliseconds
        val ONE_HOUR_IN_MILLIS: Long = 1.toDuration(DurationUnit.HOURS).inWholeMilliseconds

        private val MINUTES_IN_DAY: Long = 1.toDuration(DurationUnit.DAYS).inWholeMinutes
        private val MINUTES_IN_ALMOST_TWO_DAYS: Long = 42.toDuration(DurationUnit.HOURS).inWholeMinutes
        private val MINUTES_IN_MONTH: Long = MINUTES_IN_DAY * 30

        fun formatTimeDistance(millis: Long): String {
            val minutes = (millis / 1000).toFloat().div(60)

            if (millis <= THIRTY_SEC_IN_MILLIS) {
                return "less than a minute"
            }

            if (millis <= ONE_MINUTE_IN_MILLIS + THIRTY_SEC_IN_MILLIS) {
                return "1 minute"
            }

            if (minutes < 45) {
                return "${minutes.roundToLong()} minutes"
            }

            if (minutes < 90) {
                return "about 1 hour"
            }

            if (minutes < MINUTES_IN_DAY) {
                val hours = minutes.div(60).roundToLong()
                return "about $hours hours"
            }

            if (minutes < MINUTES_IN_ALMOST_TWO_DAYS) {
                return "1 day"
            }

            if (minutes < MINUTES_IN_MONTH) {
                val days = minutes.div(MINUTES_IN_DAY).roundToLong()
                return "$days days"
            }

            if (minutes < MINUTES_IN_MONTH * 2) {
                val months = minutes.div(MINUTES_IN_MONTH).roundToLong()
                return "about $months ${if (months == 1L) "month" else "months"}"
            }

            val months = minutes.div(MINUTES_IN_MONTH).roundToLong()

            if (months < 12) {
                return "$months months"
            }

            val monthsSinceStartOfYear = months % 12
            val years = months.floorDiv(12)

            return if (monthsSinceStartOfYear < 3) {
                "about $years ${if (years == 1L) "year" else "years"}"
            } else if (monthsSinceStartOfYear < 9) {
                "over $years ${if (years == 1L) "year" else "years"}"
            } else {
                "almost ${years + 1} years"
            }
        }
    }

    /**
     * Format hours and minutes count with localized and pluralized suffix;
     * 7260 -> "2 hours 1 minute", 7320 -> "2 hours 2 minute", 21600 -> "6 hours"
     * @param seconds Seconds to format
     *
     */
    fun hoursWithMinutesCount(seconds: Long): String {
        val hours = (seconds / SECONDS_PER_HOUR).toInt()
        val minutes = ((seconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE).toInt()

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
     * Format hours and minutes count with localized and pluralized suffix;
     * 7260 -> "2 hours 1 minute", 7320 -> "2 hours 2 minute", 21600 -> "6 hours"
     * @param seconds Seconds to format
     *
     */
    fun hoursWithMinutesCount(seconds: Float): String =
        hoursWithMinutesCount(seconds.toLong())

    /**
     * Format hours or minutes count with localized and pluralized suffix;
     * 02:03:10 -> "2 hours", 00:12:56 -> "12 minutes"
     * @param duration Duration to format
     *
     */
    fun hoursOrMinutesCount(duration: Duration): String =
        if (duration > DURATION_ONE_HOUR) {
            resourceProvider.getQuantityString(
                SharedResources.plurals.hours,
                duration.inWholeHours.toInt(),
                duration.inWholeHours.toInt()
            )
        } else {
            resourceProvider.getQuantityString(
                SharedResources.plurals.minutes,
                duration.inWholeMinutes.toInt(),
                duration.inWholeMinutes.toInt()
            )
        }

    /**
     * Format hours count (with floor rounding) with localized and pluralized suffix;
     * 7260 -> "2 hours", 7320 -> "2 hours", 21600 -> "6 hours", 3599 -> null, null -> null
     * @param seconds Seconds to format
     *
     */
    fun hoursCount(seconds: Float?): String? {
        if (seconds == null || seconds <= 0) return null

        val hours = floor(seconds / SECONDS_PER_HOUR).toInt()

        return if (hours >= 1) {
            resourceProvider.getQuantityString(SharedResources.plurals.hours, hours, hours)
        } else {
            null
        }
    }

    /**
     * Format minutes or seconds count with localized and pluralized suffix;
     * 42 -> "42 seconds", 123 -> "2 minutes"
     * @param seconds Seconds to format
     *
     */
    fun minutesOrSecondsCount(seconds: Float): String {
        val duration = seconds.toInt().toDuration(DurationUnit.SECONDS)

        return if (duration > DURATION_ONE_MINUTE) {
            resourceProvider.getQuantityString(
                SharedResources.plurals.minutes,
                duration.inWholeMinutes.toInt(),
                duration.inWholeMinutes.toInt()
            )
        } else {
            resourceProvider.getQuantityString(
                SharedResources.plurals.seconds,
                duration.inWholeSeconds.toInt(),
                duration.inWholeSeconds.toInt()
            )
        }
    }
}