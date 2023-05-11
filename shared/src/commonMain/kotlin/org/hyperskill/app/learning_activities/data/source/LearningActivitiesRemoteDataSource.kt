package org.hyperskill.app.learning_activities.data.source

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesRequest

interface LearningActivitiesRemoteDataSource {
    suspend fun getLearningActivities(
        request: LearningActivitiesRequest
    ): Result<List<LearningActivity>>
}