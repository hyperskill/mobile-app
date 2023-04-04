package org.hyperskill.app.study_plan.domain.repository

import org.hyperskill.app.study_plan.domain.model.StudyPlan

interface StudyPlanRepository {
    suspend fun getStudyPlans(): Result<List<StudyPlan>>

    suspend fun getCurrentStudyPlan(): Result<StudyPlan>
}