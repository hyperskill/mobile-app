package org.hyperskill.app.extension

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import kotlin.math.max
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class PluralsFormatter(private val resourceProvider: ResourceProvider) {
    companion object {
        private val SECONDS_PER_HOUR = 1.toDuration(DurationUnit.HOURS).inWholeSeconds
        private val SECONDS_PER_MINUTE = 1.toDuration(DurationUnit.MINUTES).inWholeSeconds
    }

    /**
     * Format hours and minutes count with localized and pluralized suffix;
     * 7260 -> "2 hours 1 minute", 7320 -> "2 hours 2 minute", 21600 -> "6 hours"
     * @param seconds Seconds to format
     *
     * */
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
            result += resourceProvider.getQuantityString(SharedResources.plurals.minutes, positiveMinutes, positiveMinutes)
        }

        return result
    }
}