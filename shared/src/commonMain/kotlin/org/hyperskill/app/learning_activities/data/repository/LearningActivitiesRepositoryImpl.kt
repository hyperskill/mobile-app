package org.hyperskill.app.learning_activities.data.repository

import org.hyperskill.app.learning_activities.data.source.LearningActivitiesRemoteDataSource
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.learning_activities.domain.repository.LearningActivitiesRepository
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesRequest

class LearningActivitiesRepositoryImpl(
    private val learningActivitiesRemoteDataSource: LearningActivitiesRemoteDataSource
) : LearningActivitiesRepository {
    override suspend fun getUncompletedTopicsLearningActivities(
        studyPlanId: Long,
        pageSize: Int,
        page: Int
    ): Result<List<LearningActivity>> =
        learningActivitiesRemoteDataSource.getLearningActivities(
            LearningActivitiesRequest(
                studyPlanId = studyPlanId,
                pageSize = pageSize,
                page = page,
                types = setOf(LearningActivityType.LEARN_TOPIC)
            )
        )

    override suspend fun getLearningActivities(
        activitiesIds: List<Long>,
        types: Set<LearningActivityType>,
        states: Set<LearningActivityState>
    ): Result<List<LearningActivity>> =
        learningActivitiesRemoteDataSource.getLearningActivities(
            LearningActivitiesRequest(
                activitiesIds = activitiesIds,
                types = types,
                states = states
            )
        )
}