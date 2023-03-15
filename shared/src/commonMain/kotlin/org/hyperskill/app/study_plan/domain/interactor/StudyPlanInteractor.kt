package org.hyperskill.app.study_plan.domain.interactor

import org.hyperskill.app.study_plan.domain.model.LearningActivity
import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.repository.LearningActivitiesRepository
import org.hyperskill.app.study_plan.domain.repository.StudyPlanRepository
import org.hyperskill.app.study_plan.domain.repository.StudyPlanSectionsRepository

class StudyPlanInteractor(
    private val studyPlanRepository: StudyPlanRepository,
    private val studyPlanSectionsRepository: StudyPlanSectionsRepository,
    private val learningActivitiesRepository: LearningActivitiesRepository
) {
    suspend fun getCurrentStudyPlan(): Result<StudyPlan> =
        studyPlanRepository.getCurrentStudyPlan()

    suspend fun getStudyPlanSections(sectionsIds: List<Long>): Result<List<StudyPlanSection>> =
        studyPlanSectionsRepository.getStudyPlanSections(sectionsIds)

    suspend fun getLearningActivities(activitiesIds: List<Long>): Result<List<LearningActivity>> =
        learningActivitiesRepository.getLearningActivities(activitiesIds)
}