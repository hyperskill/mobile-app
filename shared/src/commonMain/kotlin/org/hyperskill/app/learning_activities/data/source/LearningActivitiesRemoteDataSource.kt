package org.hyperskill.app.learning_activities.data.source

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesResponse

interface LearningActivitiesRemoteDataSource {
    suspend fun getUncompletedTopicsLearningActivities(
        studyPlanId: Long,
        pageSize: Int,
        page: Int
    ): Result<LearningActivitiesResponse>

    suspend fun getLearningActivities(
        activitiesIds: List<Long>,
        types: Set<LearningActivityType>
    ): Result<List<LearningActivity>>
}