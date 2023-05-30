package org.hyperskill.track_selection

import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackProgress
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.track.stub

fun TrackWithProgress.Companion.stub(
    trackId: Long = 0L,
    canIssueCertificate: Boolean = false,
    topicsProviders: List<Long> = emptyList()
): TrackWithProgress =
    TrackWithProgress(
        track = Track.stub(
            id = trackId,
            canIssueCertificate = canIssueCertificate,
            topicProviders = topicsProviders
        ),
        trackProgress = TrackProgress.stub(trackId)
    )