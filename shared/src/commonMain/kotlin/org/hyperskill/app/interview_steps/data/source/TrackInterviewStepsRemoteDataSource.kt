package org.hyperskill.app.interview_steps.data.source

import org.hyperskill.app.interview_steps.remote.model.TrackInterviewStepsResponse

interface TrackInterviewStepsRemoteDataSource {
    suspend fun getTrackInterviewSteps(pageSize: Int, page: Int): Result<TrackInterviewStepsResponse>
}