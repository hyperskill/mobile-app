package org.hyperskill.app.core.view.mapper

import org.hyperskill.app.SharedResources

class NumbersFormatter(private val resourceProvider: ResourceProvider) {
    fun formatProgressAverageRating(averageRating: Double): String =
        if (averageRating > 0.0) {
            averageRating.toString().take(3)
        } else {
            // decimal formatting in kotlin stdlib https://youtrack.jetbrains.com/issue/KT-21644
            resourceProvider.getString(SharedResources.strings.new_word)
        }
}