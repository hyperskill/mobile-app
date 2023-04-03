package org.hyperskill.app.progresses.domain.repository

import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.topics.domain.model.TopicProgress
import org.hyperskill.app.track.domain.model.TrackProgress

interface ProgressesRepository {
    suspend fun getTracksProgresses(tracksIds: List<Long>, force: Boolean): Result<List<TrackProgress>>

    suspend fun getTrackProgress(trackId: Long, force: Boolean): Result<TrackProgress?> =
        getTracksProgresses(listOf(trackId), force).map { it.firstOrNull() }

    suspend fun getTopicsProgresses(topicsIds: List<Long>): Result<List<TopicProgress>>

    suspend fun getTopicProgress(topicId: Long): Result<TopicProgress> =
        kotlin.runCatching {
            getTopicsProgresses(listOf(topicId)).getOrThrow().first()
        }

    suspend fun getProjectsProgresses(projectsIds: List<Long>, force: Boolean): Result<List<ProjectProgress>>

    suspend fun getProjectProgress(projectId: Long, force: Boolean): Result<ProjectProgress> =
        kotlin.runCatching {
            getProjectsProgresses(listOf(projectId), force).getOrThrow().first()
        }
}