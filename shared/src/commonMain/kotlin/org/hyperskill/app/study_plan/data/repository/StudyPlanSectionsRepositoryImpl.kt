package org.hyperskill.app.study_plan.data.repository

import org.hyperskill.app.study_plan.data.source.StudyPlanSectionsRemoteDataSource
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.repository.StudyPlanSectionsRepository

class StudyPlanSectionsRepositoryImpl(
    private val remoteDataSource: StudyPlanSectionsRemoteDataSource
) : StudyPlanSectionsRepository {
    override suspend fun getStudyPlanSections(sectionsIds: List<Long>): Result<List<StudyPlanSection>> =
        remoteDataSource.getStudyPlanSections(sectionsIds)
}