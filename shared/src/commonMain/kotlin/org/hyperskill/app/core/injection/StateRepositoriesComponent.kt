package org.hyperskill.app.core.injection

import org.hyperskill.app.learning_activities.domain.repository.NextLearningActivityStateRepository
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository

interface StateRepositoriesComponent {
    // Note: add state reset of every new state repository to resetStateRepositories method
    val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository

    val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository

    val nextLearningActivityStateRepository: NextLearningActivityStateRepository

    suspend fun resetRepositories() {
        currentSubscriptionStateRepository.resetState()
        currentStudyPlanStateRepository.resetState()
        nextLearningActivityStateRepository.resetState()
    }
}