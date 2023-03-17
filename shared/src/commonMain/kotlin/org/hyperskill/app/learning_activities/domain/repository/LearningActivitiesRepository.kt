package org.hyperskill.app.learning_activities.domain.repository

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesResponse

interface LearningActivitiesRepository {
    suspend fun getUncompletedTopicsLearningActivities(
        studyPlanId: Long,
        pageSize: Int = 10,
        page: Int = 1
    ): Result<LearningActivitiesResponse>

    suspend fun getLearningActivities(
        activitiesIds: List<Long>,
        types: Set<LearningActivityType>
    ): Result<List<LearningActivity>>
}