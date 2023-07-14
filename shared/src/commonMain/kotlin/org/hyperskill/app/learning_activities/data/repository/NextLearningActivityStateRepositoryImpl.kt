package org.hyperskill.app.learning_activities.data.repository

import org.hyperskill.app.core.data.repository.BaseStateRepository
import org.hyperskill.app.learning_activities.data.source.LearningActivitiesRemoteDataSource
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.repository.NextLearningActivityStateRepository
import org.hyperskill.app.learning_activities.remote.model.NextLearningActivityRequest

class NextLearningActivityStateRepositoryImpl(
    private val learningActivitiesRemoteDataSource: LearningActivitiesRemoteDataSource
) : NextLearningActivityStateRepository, BaseStateRepository<LearningActivity>() {
    override suspend fun loadState(): Result<LearningActivity> =
        learningActivitiesRemoteDataSource.getNextLearningActivity(NextLearningActivityRequest())
}