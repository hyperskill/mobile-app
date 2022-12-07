package org.hyperskill.app.track.domain.repository

import org.hyperskill.app.track.domain.model.StudyPlan
import org.hyperskill.app.track.domain.model.Track

interface TrackRepository {
    suspend fun getTrack(trackId: Long): Result<Track> =
        getTracks(listOf(trackId)).map { it.first() }

    suspend fun getTracks(trackIds: List<Long>?): Result<List<Track>>

    suspend fun getStudyPlans(): Result<List<StudyPlan>>

    suspend fun getStudyPlanByTrackId(trackId: Long): Result<StudyPlan> =
        getStudyPlans().map { it.first { plan -> plan.trackId == trackId } }
}