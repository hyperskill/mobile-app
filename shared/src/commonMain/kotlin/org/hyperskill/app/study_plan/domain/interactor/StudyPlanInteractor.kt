package org.hyperskill.app.study_plan.domain.interactor

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.learning_activities.domain.repository.LearningActivitiesRepository
import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.study_plan.domain.repository.StudyPlanSectionsRepository

class StudyPlanInteractor(
    private val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
    private val studyPlanSectionsRepository: StudyPlanSectionsRepository,
    private val learningActivitiesRepository: LearningActivitiesRepository
) {
    suspend fun getCurrentStudyPlan(forceLoadFromRemote: Boolean): Result<StudyPlan> =
        currentStudyPlanStateRepository.getState(forceLoadFromRemote)

    suspend fun getStudyPlanSections(sectionsIds: List<Long>): Result<List<StudyPlanSection>> =
        studyPlanSectionsRepository.getStudyPlanSections(sectionsIds)

    suspend fun getLearningActivities(
        activitiesIds: List<Long>,
        types: Set<LearningActivityType>,
        states: Set<LearningActivityState>
    ): Result<List<LearningActivity>> =
        learningActivitiesRepository.getLearningActivities(activitiesIds, types, states)
}