package org.hyperskill.app.core.view.mapper

import org.hyperskill.app.SharedResources

class NumbersFormatter(private val resourceProvider: ResourceProvider) {
    companion object {
        fun formatFloatingNumber(number: Double, decimalPoints: Int = 2): String {
            // decimal formatting in kotlin stdlib https://youtrack.jetbrains.com/issue/KT-21644
            val numberParts = number.toString().split(".")
            return if (numberParts.size == 2) {
                val decimalPart = numberParts[1].take(decimalPoints)
                "${numberParts[0]}.$decimalPart"
            } else {
                number.toString()
            }
        }
    }

    fun formatProgressAverageRating(averageRating: Double): String =
        if (averageRating > 0.0) {
            formatFloatingNumber(averageRating, decimalPoints = 1)
        } else {
            resourceProvider.getString(SharedResources.strings.new_word)
        }
}