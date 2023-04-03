package org.hyperskill.app.progresses.data.repository

import org.hyperskill.app.core.data.repository_cache.RepositoryCacheProxy
import org.hyperskill.app.progresses.data.source.ProgressesRemoteDataSource
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.projects.domain.model.projectId
import org.hyperskill.app.topics.domain.model.TopicProgress
import org.hyperskill.app.track.domain.model.TrackProgress
import org.hyperskill.app.track.domain.model.trackId

class ProgressesRepositoryImpl(
    private val progressesRemoteDataSource: ProgressesRemoteDataSource
) : ProgressesRepository {

    private val trackProgressCacheProxy = RepositoryCacheProxy(
        loadFromRemoteValues = { trackIds ->
            progressesRemoteDataSource
                .getTracksProgresses(trackIds)
        },
        getKey = { trackProgress ->
            trackProgress.trackId
        }
    )

    private val projectProgressCacheProxy = RepositoryCacheProxy(
        loadFromRemoteValues = { projectIds ->
            progressesRemoteDataSource
                .getProjectsProgresses(projectIds)
        },
        getKey = { projectProgress ->
            projectProgress.projectId
        }
    )

    override suspend fun getTracksProgresses(
        tracksIds: List<Long>,
        force: Boolean
    ): Result<List<TrackProgress>> =
        trackProgressCacheProxy.getValues(tracksIds, force)

    override suspend fun getTopicsProgresses(topicsIds: List<Long>): Result<List<TopicProgress>> =
        progressesRemoteDataSource.getTopicsProgresses(topicsIds)

    override suspend fun getProjectsProgresses(projectsIds: List<Long>, force: Boolean): Result<List<ProjectProgress>> =
        projectProgressCacheProxy.getValues(projectsIds, force)
}