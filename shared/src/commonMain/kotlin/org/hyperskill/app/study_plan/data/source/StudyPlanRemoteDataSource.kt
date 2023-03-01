package org.hyperskill.app.study_plan.data.source

import org.hyperskill.app.study_plan.domain.model.StudyPlan

interface StudyPlanRemoteDataSource {
    suspend fun getStudyPlans(): Result<List<StudyPlan>>

    suspend fun getCurrentStudyPlan(): Result<StudyPlan>
}