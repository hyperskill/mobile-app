package org.hyperskill.app.study_plan.domain.repository

import org.hyperskill.app.study_plan.domain.model.LearningActivity

interface LearningActivitiesRepository {
    suspend fun getLearningActivities(activitiesIds: List<Long>): Result<List<LearningActivity>>
}