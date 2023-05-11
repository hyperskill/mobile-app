package org.hyperskill.app.study_plan.domain.repository

import org.hyperskill.app.study_plan.domain.model.StudyPlanSection

interface StudyPlanSectionsRepository {
    suspend fun getStudyPlanSections(sectionsIds: List<Long>): Result<List<StudyPlanSection>>
}