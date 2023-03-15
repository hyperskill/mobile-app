package org.hyperskill.app.study_plan.data.repository

import org.hyperskill.app.study_plan.domain.model.LearningActivity
import org.hyperskill.app.study_plan.domain.repository.LearningActivitiesRepository
import org.hyperskill.app.study_plan.remote.LearningActivitiesRemoteDataSourceImpl

class LearningActivitiesRepositoryImpl(
    private val remoteDataSource: LearningActivitiesRemoteDataSourceImpl
) : LearningActivitiesRepository {
    override suspend fun getLearningActivities(activitiesIds: List<Long>): Result<List<LearningActivity>> =
        remoteDataSource.getLearningActivities(activitiesIds)
}