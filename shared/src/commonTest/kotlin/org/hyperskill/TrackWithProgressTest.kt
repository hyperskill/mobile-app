package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.track_selection.stub

class TrackWithProgressTest {
    @Test
    fun testTrackWithProgressCompletedTopicsProgressPercentage() {
        val trackWithProgress = TrackWithProgress.stub(
            learnedTopicsCount = 2,
            skippedTopicsCount = 1,
            topicsCount = 5
        )
        assertEquals(60, trackWithProgress.completedTopicsProgress)
    }

    @Test
    fun testTrackWithProgressAppliedTopicsProgressPercentage() {
        val trackWithProgress = TrackWithProgress.stub(
            appliedCapstoneTopicsCount = 3,
            capstoneTopicsCount = 6
        )
        assertEquals(50, trackWithProgress.appliedTopicsProgress)
    }
}