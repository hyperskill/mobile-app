package org.hyperskill.app.learning_activities.domain.repository

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesWithSectionsResponse
import org.hyperskill.app.study_plan.domain.model.StudyPlanSectionType

interface LearningActivitiesRepository {
    suspend fun getUncompletedTopicsLearningActivities(
        studyPlanId: Long,
        pageSize: Int = 10,
        page: Int = 1
    ): Result<List<LearningActivity>>

    suspend fun getLearningActivities(
        activitiesIds: List<Long>,
        types: Set<LearningActivityType>,
        states: Set<LearningActivityState>
    ): Result<List<LearningActivity>>

    suspend fun getNextLearningActivity(
        types: Set<LearningActivityType>
    ): Result<LearningActivity?>

    suspend fun getLearningActivitiesWithSections(
        studyPlanSectionTypes: Set<StudyPlanSectionType>,
        learningActivityTypes: Set<LearningActivityType>,
        learningActivityStates: Set<LearningActivityState>
    ): Result<LearningActivitiesWithSectionsResponse>
}