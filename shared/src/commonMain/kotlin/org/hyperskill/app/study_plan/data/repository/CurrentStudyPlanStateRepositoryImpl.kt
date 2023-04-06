package org.hyperskill.app.study_plan.data.repository

import org.hyperskill.app.core.data.repository.BaseStateRepository
import org.hyperskill.app.study_plan.data.source.StudyPlanRemoteDataSource
import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository

class CurrentStudyPlanStateRepositoryImpl(
    private val studyPlanRemoteDataSource: StudyPlanRemoteDataSource
) : CurrentStudyPlanStateRepository, BaseStateRepository<StudyPlan>() {
    override suspend fun loadState(): Result<StudyPlan> =
        studyPlanRemoteDataSource.getCurrentStudyPlan()
}