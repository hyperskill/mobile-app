package org.hyperskill.track_selection

import org.hyperskill.app.track.domain.model.TrackProgress

fun TrackProgress.Companion.stub(
    trackId: Long,
    clarity: Float? = null,
    funMeasure: Float? = null,
    usefulness: Float? = null
): TrackProgress =
    TrackProgress(
        id = "",
        vid = "track-$trackId",
        isCompleted = false,
        clarity = clarity,
        funMeasure = funMeasure,
        usefulness = usefulness,
        completedProjects = emptyList(),
        completedCapstoneProjects = emptyList(),
        appliedCapstoneTopicsCount = 0,
        learnedTopicsCount = 0,
        skippedTopicsCount = 0,
    )