package org.hyperskill.app.study_plan.data.repository

import org.hyperskill.app.study_plan.data.source.StudyPlanRemoteDataSource
import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.study_plan.domain.repository.StudyPlanRepository

class StudyPlanRepositoryImpl(
    private val studyPlanRemoteDataSource: StudyPlanRemoteDataSource
) : StudyPlanRepository {
    override suspend fun getStudyPlans(): Result<List<StudyPlan>> =
        studyPlanRemoteDataSource.getStudyPlans()

    override suspend fun getCurrentStudyPlan(): Result<StudyPlan> =
        studyPlanRemoteDataSource.getCurrentStudyPlan()
}