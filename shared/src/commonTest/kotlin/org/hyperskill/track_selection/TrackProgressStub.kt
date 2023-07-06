package org.hyperskill.track_selection

import org.hyperskill.app.track.domain.model.TrackProgress

fun TrackProgress.Companion.stub(
    trackId: Long,
    clarity: Float? = null,
    funMeasure: Float? = null,
    usefulness: Float? = null,
    learnedTopicsCount: Int = 0,
    skippedTopicsCount: Int = 0,
    appliedCapstoneTopicsCount: Int = 0
): TrackProgress =
    TrackProgress(
        id = "",
        vid = "track-$trackId",
        isCompleted = false,
        clarity = clarity,
        funMeasure = funMeasure,
        usefulness = usefulness,
        learnedTopicsCount = learnedTopicsCount,
        skippedTopicsCount = skippedTopicsCount,
        appliedCapstoneTopicsCount = appliedCapstoneTopicsCount
    )