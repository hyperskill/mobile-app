package org.hyperskill.app.progresses.data.repository

import org.hyperskill.app.core.domain.repository_cache.RepositoryCacheProxy
import org.hyperskill.app.progresses.data.source.ProgressesRemoteDataSource
import org.hyperskill.app.progresses.data.source.ProjectProgressesCacheDataSource
import org.hyperskill.app.progresses.data.source.TopicProgressesCacheDataSource
import org.hyperskill.app.progresses.data.source.TrackProgressesCacheDataSource
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.projects.domain.model.projectId
import org.hyperskill.app.topics.domain.model.TopicProgress
import org.hyperskill.app.topics.domain.model.topicId
import org.hyperskill.app.track.domain.model.TrackProgress
import org.hyperskill.app.track.domain.model.trackId

internal class ProgressesRepositoryImpl(
    private val progressesRemoteDataSource: ProgressesRemoteDataSource,
    trackProgressesCacheDataSource: TrackProgressesCacheDataSource,
    topicProgressesCacheDataSource: TopicProgressesCacheDataSource,
    projectProgressesCacheDataSource: ProjectProgressesCacheDataSource
) : ProgressesRepository {

    private val trackProgressCacheProxy = RepositoryCacheProxy(
        cache = trackProgressesCacheDataSource,
        loadValuesFromRemote = { tracksIds ->
            progressesRemoteDataSource
                .getTracksProgresses(tracksIds)
        },
        getKeyFromValue = { trackProgress ->
            trackProgress.trackId
        }
    )

    private val topicProgressesCacheProxy = RepositoryCacheProxy(
        cache = topicProgressesCacheDataSource,
        loadValuesFromRemote = { topicsIds ->
            progressesRemoteDataSource
                .getTopicsProgresses(topicsIds)
        },
        getKeyFromValue = { topicProgress ->
            topicProgress.topicId
        }
    )

    private val projectProgressCacheProxy = RepositoryCacheProxy(
        cache = projectProgressesCacheDataSource,
        loadValuesFromRemote = { projectsIds ->
            progressesRemoteDataSource
                .getProjectsProgresses(projectsIds)
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

    override suspend fun getTopicsProgresses(
        topicsIds: List<Long>,
        forceLoadFromRemote: Boolean
    ): Result<List<TopicProgress>> =
        topicProgressesCacheProxy.getValues(topicsIds, forceLoadFromRemote)

    override suspend fun getProjectsProgresses(
        projectsIds: List<Long>,
        forceLoadFromRemote: Boolean
    ): Result<List<ProjectProgress>> =
        projectProgressCacheProxy.getValues(projectsIds, forceLoadFromRemote)

    override suspend fun putTopicsProgressesToCache(progresses: List<TopicProgress>) {
        topicProgressesCacheProxy.putValues(progresses)
    }

    override fun clearCache() {
        trackProgressCacheProxy.clearCache()
        topicProgressesCacheProxy.clearCache()
        projectProgressCacheProxy.clearCache()
    }
}