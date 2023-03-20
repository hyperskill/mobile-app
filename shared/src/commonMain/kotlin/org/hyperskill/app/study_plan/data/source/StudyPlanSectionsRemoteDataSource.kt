package org.hyperskill.app.study_plan.data.source

import org.hyperskill.app.study_plan.domain.model.StudyPlanSection

interface StudyPlanSectionsRemoteDataSource {
    suspend fun getStudyPlanSections(sectionsIds: List<Long>): Result<List<StudyPlanSection>>
}