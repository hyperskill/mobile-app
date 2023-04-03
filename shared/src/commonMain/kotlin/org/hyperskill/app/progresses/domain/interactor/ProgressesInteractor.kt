package org.hyperskill.app.progresses.domain.interactor

import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.topics.domain.model.TopicProgress
import org.hyperskill.app.track.domain.model.TrackProgress

class ProgressesInteractor(
    private val progressesRepository: ProgressesRepository
) {
    suspend fun getTracksProgresses(tracksIds: List<Long>, force: Boolean): Result<List<TrackProgress>> =
        progressesRepository.getTracksProgresses(tracksIds, force)

    suspend fun getTrackProgress(trackId: Long, force: Boolean): Result<TrackProgress?> =
        progressesRepository.getTrackProgress(trackId, force)

    suspend fun getTopicsProgresses(topicsIds: List<Long>): Result<List<TopicProgress>> =
        progressesRepository.getTopicsProgresses(topicsIds)

    suspend fun getTopicProgress(topicId: Long): Result<TopicProgress> =
        progressesRepository.getTopicProgress(topicId)

    suspend fun getProjectsProgresses(projectsIds: List<Long>, force: Boolean): Result<List<ProjectProgress>> =
        progressesRepository.getProjectsProgresses(projectsIds, force)

    suspend fun getProjectProgress(projectId: Long, force: Boolean): Result<ProjectProgress> =
        progressesRepository.getProjectProgress(projectId, force)
}