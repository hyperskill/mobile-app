package org.hyperskill.app.learning_activities.data.source

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesRequest
import org.hyperskill.app.learning_activities.remote.model.NextLearningActivityRequest

interface LearningActivitiesRemoteDataSource {
    suspend fun getNextLearningActivity(
        request: NextLearningActivityRequest
    ): Result<LearningActivity?>

    suspend fun getLearningActivities(
        request: LearningActivitiesRequest
    ): Result<List<LearningActivity>>
}