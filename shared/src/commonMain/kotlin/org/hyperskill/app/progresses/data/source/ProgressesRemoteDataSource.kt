package org.hyperskill.app.progress_screen.data.source

import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.topics.domain.model.TopicProgress
import org.hyperskill.app.track.domain.model.TrackProgress

interface ProgressesRemoteDataSource {
    suspend fun getTracksProgresses(tracksIds: List<Long>): Result<List<TrackProgress>>
    suspend fun getTopicsProgresses(topicsIds: List<Long>): Result<List<TopicProgress>>
    suspend fun getProjectsProgresses(projectsIds: List<Long>): Result<List<ProjectProgress>>
}