package org.hyperskill.app.core.injection

import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository

interface SingletonRepositoriesComponent {
    // Note: add state reset of every new state repository to resetStateRepositories method

    /**
     * State repositories
     */
    val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository

    /**
     * Cached list repositories
     */
    val progressesRepository: ProgressesRepository

    suspend fun resetRepositories() {
        currentSubscriptionStateRepository.resetState()
        progressesRepository.clearCache()
    }
}