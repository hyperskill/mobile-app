package org.hyperskill.app.study_plan.domain.interactor

import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.study_plan.domain.repository.StudyPlanRepository

class StudyPlanInteractor(
    private val studyPlanRepository: StudyPlanRepository
) {
    suspend fun getCurrentStudyPlan(): Result<StudyPlan> =
        studyPlanRepository.getCurrentStudyPlan()
}