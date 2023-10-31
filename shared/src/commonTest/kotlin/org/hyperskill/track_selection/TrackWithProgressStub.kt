package org.hyperskill.track_selection

import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackProgress
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.track.stub

fun TrackWithProgress.Companion.stub(
    trackId: Long = 0L,
    canIssueCertificate: Boolean = false,
    topicsProviders: List<Long> = emptyList(),
    learnedTopicsCount: Int = 0,
    skippedTopicsCount: Int = 0,
    topicsCount: Int = 1,
    appliedCapstoneTopicsCount: Int = 0,
    capstoneTopicsCount: Int = 0,
    projects: List<Long> = emptyList()
): TrackWithProgress =
    TrackWithProgress(
        track = Track.stub(
            id = trackId,
            canIssueCertificate = canIssueCertificate,
            topicProviders = topicsProviders,
            topicsCount = topicsCount,
            capstoneTopicsCount = capstoneTopicsCount,
            projects = projects
        ),
        trackProgress = TrackProgress.stub(
            trackId,
            learnedTopicsCount = learnedTopicsCount,
            skippedTopicsCount = skippedTopicsCount,
            appliedCapstoneTopicsCount = appliedCapstoneTopicsCount
        )
    )