package org.hyperskill.app.core.injection

import org.hyperskill.app.progresses.data.source.ProjectProgressesCacheDataSource
import org.hyperskill.app.progresses.data.source.TrackProgressesCacheDataSource
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.track.data.source.TrackCacheDataSource

interface SingletonRepositoriesComponent {
    // Note: add state reset of every new state repository to resetStateRepositories method

    /**
     * State repositories
     */
    val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository

    val studyPlanStateRepository: CurrentStudyPlanStateRepository

    /**
     * Repositories cache
     */
    val trackProgressesCacheDataSource: TrackProgressesCacheDataSource

    val projectProgressesCacheDataSource: ProjectProgressesCacheDataSource

    val trackCacheDataSource: TrackCacheDataSource

    suspend fun resetRepositories() {
        currentSubscriptionStateRepository.resetState()
        studyPlanStateRepository.resetState()
        trackProgressesCacheDataSource.clearCache()
        projectProgressesCacheDataSource.clearCache()
    }
}