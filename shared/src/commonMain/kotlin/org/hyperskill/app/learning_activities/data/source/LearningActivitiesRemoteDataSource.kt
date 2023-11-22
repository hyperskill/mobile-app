package org.hyperskill.app.learning_activities.data.source

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesRequest
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesWithSectionsRequest
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesWithSectionsResponse
import org.hyperskill.app.learning_activities.remote.model.NextLearningActivityRequest

internal interface LearningActivitiesRemoteDataSource {
    suspend fun getNextLearningActivity(
        request: NextLearningActivityRequest
    ): Result<LearningActivity?>

    suspend fun getLearningActivities(
        request: LearningActivitiesRequest
    ): Result<List<LearningActivity>>

    suspend fun getLearningActivitiesWithSections(
        request: LearningActivitiesWithSectionsRequest
    ): Result<LearningActivitiesWithSectionsResponse>
}