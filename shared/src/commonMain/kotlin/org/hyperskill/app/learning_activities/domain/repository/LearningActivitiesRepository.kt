package org.hyperskill.app.learning_activities.domain.repository

import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesResponse

interface LearningActivitiesRepository {
    suspend fun getUncompletedTopicsLearningActivities(
        pageSize: Int = 10,
        page: Int = 1
    ): Result<LearningActivitiesResponse>
}