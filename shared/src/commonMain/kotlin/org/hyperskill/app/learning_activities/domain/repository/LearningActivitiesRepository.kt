package org.hyperskill.app.learning_activities.domain.repository

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType

interface LearningActivitiesRepository {
    suspend fun getUncompletedTopicsLearningActivities(
        studyPlanId: Long,
        pageSize: Int = 10,
        page: Int = 1
    ): Result<List<LearningActivity>>

    suspend fun getLearningActivities(
        activitiesIds: List<Long>,
        types: Set<LearningActivityType>
    ): Result<List<LearningActivity>>
}