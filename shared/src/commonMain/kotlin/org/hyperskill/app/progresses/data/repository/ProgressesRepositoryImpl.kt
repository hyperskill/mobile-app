package org.hyperskill.app.progresses.data.repository

import org.hyperskill.app.core.domain.repository_cache.RepositoryCacheProxy
import org.hyperskill.app.progresses.data.source.ProgressesRemoteDataSource
import org.hyperskill.app.progresses.data.source.ProjectProgressesCacheDataSource
import org.hyperskill.app.progresses.data.source.TrackProgressesCacheDataSource
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.projects.domain.model.projectId
import org.hyperskill.app.topics.domain.model.TopicProgress
import org.hyperskill.app.track.domain.model.TrackProgress
import org.hyperskill.app.track.domain.model.trackId

class ProgressesRepositoryImpl(
    private val progressesRemoteDataSource: ProgressesRemoteDataSource,
    trackProgressesCacheDataSource: TrackProgressesCacheDataSource,
    projectProgressesCacheDataSource: ProjectProgressesCacheDataSource
) : ProgressesRepository {

    private val trackProgressCacheProxy = RepositoryCacheProxy(
        cache = trackProgressesCacheDataSource,
        loadValuesFromRemote = { trackIds ->
            progressesRemoteDataSource
                .getTracksProgresses(trackIds)
        },
        getKeyFromValue = { trackProgress ->
            trackProgress.trackId
        }
    )

    private val projectProgressCacheProxy = RepositoryCacheProxy(
        cache = projectProgressesCacheDataSource,
        loadValuesFromRemote = { projectIds ->
            progressesRemoteDataSource
                .getProjectsProgresses(projectIds)
        },
        getKeyFromValue = { projectProgress ->
            projectProgress.projectId
        }
    )

    override suspend fun getTracksProgresses(
        tracksIds: List<Long>,
        forceLoadFromRemote: Boolean
    ): Result<List<TrackProgress>> =
        trackProgressCacheProxy.getValues(tracksIds, forceLoadFromRemote)

    override suspend fun getTopicsProgresses(topicsIds: List<Long>): Result<List<TopicProgress>> =
        progressesRemoteDataSource.getTopicsProgresses(topicsIds)

    override suspend fun getProjectsProgresses(
        projectsIds: List<Long>,
        forceLoadFromRemote: Boolean
    ): Result<List<ProjectProgress>> =
        projectProgressCacheProxy.getValues(projectsIds, forceLoadFromRemote)

    override fun clearCache() {
        trackProgressCacheProxy.clearCache()
        projectProgressCacheProxy.clearCache()
    }
}