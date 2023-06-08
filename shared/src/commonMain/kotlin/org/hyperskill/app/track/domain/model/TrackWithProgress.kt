package org.hyperskill.app.track.domain.model

import kotlin.math.floor
import kotlinx.serialization.Serializable

@Serializable
data class TrackWithProgress(
    val track: Track,
    val trackProgress: TrackProgress
) {
    companion object

    val averageProgress: Int
        get() {
            val currentTopicsCount =
                trackProgress.learnedTopicsCount +
                    trackProgress.skippedTopicsCount +
                    trackProgress.appliedCapstoneTopicsCount
            val maxTopicsCount =
                track.topicsCount + track.capstoneTopicsCount
            return floor(currentTopicsCount / maxTopicsCount.toFloat() * 100).toInt()
        }
}