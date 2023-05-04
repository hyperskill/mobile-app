package org.hyperskill.app.learning_activities.domain.interactor

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.repository.LearningActivitiesRepository

class LearningActivitiesInteractor(
    private val learningActivitiesRepository: LearningActivitiesRepository
) {
    suspend fun getUncompletedTopicsLearningActivities(
        studyPlanId: Long,
        pageSize: Int = 10,
        page: Int = 1
    ): Result<List<LearningActivity>> =
        learningActivitiesRepository.getUncompletedTopicsLearningActivities(studyPlanId, pageSize, page)
}