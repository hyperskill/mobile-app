package org.hyperskill.app.study_plan.injection

import org.hyperskill.app.study_plan.domain.interactor.StudyPlanInteractor
import org.hyperskill.app.study_plan.domain.repository.StudyPlanRepository

interface StudyPlanDataComponent {
    val studyPlanRepository: StudyPlanRepository
    val studyPlanInteractor: StudyPlanInteractor
}