package org.hyperskill.app.progresses.domain.model

import kotlin.math.roundToInt

interface Progress {
    val clarity: Float?
    val funMeasure: Float?
    val usefulness: Float?
}

fun Progress.averageRating(): Double {
    val internalFunMeasure = (funMeasure ?: 0).toDouble()
    val internalClarity = (clarity ?: 0).toDouble()
    val internalUsefulness = (usefulness ?: 0).toDouble()
    val avgRating = (internalFunMeasure + internalClarity + internalUsefulness) / 3

    return (avgRating * 10).roundToInt() / 10.0
}