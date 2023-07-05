package org.hyperskill.app.progress_screen.domain.interactor

import org.hyperskill.app.progress_screen.domain.repository.ProgressesRepository
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.topics.domain.model.TopicProgress
import org.hyperskill.app.track.domain.model.TrackProgress

class ProgressesInteractor(
    private val progressesRepository: ProgressesRepository
) {
    suspend fun getTracksProgresses(tracksIds: List<Long>, forceLoadFromRemote: Boolean): Result<List<TrackProgress>> =
        progressesRepository.getTracksProgresses(tracksIds, forceLoadFromRemote)

    suspend fun getTrackProgress(trackId: Long, forceLoadFromRemote: Boolean): Result<TrackProgress?> =
        progressesRepository.getTrackProgress(trackId, forceLoadFromRemote)

    suspend fun getTopicsProgresses(topicsIds: List<Long>): Result<List<TopicProgress>> =
        progressesRepository.getTopicsProgresses(topicsIds)

    suspend fun getTopicProgress(topicId: Long): Result<TopicProgress> =
        progressesRepository.getTopicProgress(topicId)

    suspend fun getProjectsProgresses(
        projectsIds: List<Long>,
        forceLoadFromRemote: Boolean
    ): Result<List<ProjectProgress>> =
        progressesRepository.getProjectsProgresses(projectsIds, forceLoadFromRemote)

    suspend fun getProjectProgress(projectId: Long, forceLoadFromRemote: Boolean): Result<ProjectProgress> =
        progressesRepository.getProjectProgress(projectId, forceLoadFromRemote)
}