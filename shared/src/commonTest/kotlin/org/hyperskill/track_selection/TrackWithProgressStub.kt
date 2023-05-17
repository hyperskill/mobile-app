package org.hyperskill.track_selection

import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackProgress
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.track.stub

fun TrackWithProgress.Companion.stub(
    trackId: Long = 0L
): TrackWithProgress =
    TrackWithProgress(
        track = Track.stub(trackId),
        trackProgress = TrackProgress.stub(trackId)
    )