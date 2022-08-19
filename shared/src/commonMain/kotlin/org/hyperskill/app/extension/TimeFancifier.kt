package org.hyperskill.app.extension

import kotlin.math.roundToLong

class TimeFancifier {
    companion object {
        const val THIRTY_SEC_IN_MILLIS: Long = 30 * 1000
        const val ONE_MINUTE_IN_MILLIS: Long = THIRTY_SEC_IN_MILLIS * 2
        const val ONE_HOUR_IN_MILLIS: Long = ONE_MINUTE_IN_MILLIS * 60

        const val MINUTES_IN_DAY: Long = 24 * 60
        const val MINUTES_IN_ALMOST_TWO_DAYS: Long = 42 * 60
        const val MINUTES_IN_MONTH: Long = MINUTES_IN_DAY * 30

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
}