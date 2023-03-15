package org.hyperskill.app.study_plan.data.source

import org.hyperskill.app.study_plan.domain.model.LearningActivity

interface LearningActivitiesRemoteDataSource {
    suspend fun getLearningActivities(activitiesIds: List<Long>): Result<List<LearningActivity>>
}