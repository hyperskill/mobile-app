package org.hyperskill.app.learning_activities.data.source

import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesResponse

interface LearningActivitiesRemoteDataSource {
    suspend fun getUncompletedTopicsLearningActivities(
        studyPlanId: Long,
        pageSize: Int,
        page: Int
    ): Result<LearningActivitiesResponse>
}