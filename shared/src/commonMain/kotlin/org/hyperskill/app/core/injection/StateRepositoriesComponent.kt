package org.hyperskill.app.core.injection

import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository

interface StateRepositoriesComponent {
    // Note: add state reset of every new state repository to resetStateRepositories method
    val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository

    suspend fun resetRepositories() {
        currentSubscriptionStateRepository.resetState()
    }
}