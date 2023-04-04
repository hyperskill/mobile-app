package org.hyperskill.app.core.injection

import org.hyperskill.app.progresses.data.source.ProjectProgressesCacheDataSource
import org.hyperskill.app.progresses.data.source.TrackProgressesCacheDataSource
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository

interface SingletonRepositoriesComponent {
    // Note: add state reset of every new state repository to resetStateRepositories method

    /**
     * State repositories
     */
    val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository

    /**
     * Repositories cache
     */
    val trackProgressesCacheDataSource: TrackProgressesCacheDataSource

    val projectProgressesCacheDataSource: ProjectProgressesCacheDataSource

    suspend fun resetRepositories() {
        currentSubscriptionStateRepository.resetState()
        trackProgressesCacheDataSource.clearCache()
        projectProgressesCacheDataSource.clearCache()
    }
}