package org.hyperskill.app.learning_activities.domain.interactor

import org.hyperskill.app.learning_activities.domain.repository.LearningActivitiesRepository
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesResponse

class LearningActivitiesInteractor(
    private val learningActivitiesRepository: LearningActivitiesRepository
) {
    suspend fun getUncompletedTopicsLearningActivities(
        pageSize: Int = 10,
        page: Int = 1
    ): Result<LearningActivitiesResponse> =
        learningActivitiesRepository.getUncompletedTopicsLearningActivities(pageSize, page)
}