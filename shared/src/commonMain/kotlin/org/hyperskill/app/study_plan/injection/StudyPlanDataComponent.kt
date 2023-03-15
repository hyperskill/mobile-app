package org.hyperskill.app.study_plan.injection

import org.hyperskill.app.study_plan.domain.interactor.StudyPlanInteractor
import org.hyperskill.app.study_plan.domain.repository.LearningActivitiesRepository
import org.hyperskill.app.study_plan.domain.repository.StudyPlanRepository
import org.hyperskill.app.study_plan.domain.repository.StudyPlanSectionsRepository

interface StudyPlanDataComponent {
    val studyPlanRepository: StudyPlanRepository
    val studyPlanSectionsRepository: StudyPlanSectionsRepository
    val learningActivitiesRepository: LearningActivitiesRepository
    val studyPlanInteractor: StudyPlanInteractor
}