package org.hyperskill.app.learning_activities.data.repository

import org.hyperskill.app.learning_activities.data.source.LearningActivitiesRemoteDataSource
import org.hyperskill.app.learning_activities.domain.repository.LearningActivitiesRepository
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesResponse

class LearningActivitiesRepositoryImpl(
    private val learningActivitiesRemoteDataSource: LearningActivitiesRemoteDataSource
) : LearningActivitiesRepository {
    override suspend fun getUncompletedTopicsLearningActivities(
        pageSize: Int,
        page: Int
    ): Result<LearningActivitiesResponse> =
        learningActivitiesRemoteDataSource.getUncompletedTopicsLearningActivities(pageSize, page)
}