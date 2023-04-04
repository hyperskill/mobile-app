package org.hyperskill.app.study_plan.domain.interactor

import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository

class StudyPlanInteractor(
    private val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository
) {
    suspend fun getCurrentStudyPlan(forceLoadFromRemote: Boolean): Result<StudyPlan> =
        currentStudyPlanStateRepository.getState(forceLoadFromRemote)
}