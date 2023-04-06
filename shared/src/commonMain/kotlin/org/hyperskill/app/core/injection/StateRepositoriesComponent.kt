package org.hyperskill.app.core.injection

import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository

interface StateRepositoriesComponent {
    // Note: add state reset of every new state repository to resetStateRepositories method
    val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository

    val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository

    suspend fun resetRepositories() {
        currentSubscriptionStateRepository.resetState()
        currentStudyPlanStateRepository.resetState()
    }
}