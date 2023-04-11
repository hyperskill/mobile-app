package org.hyperskill.app.study_plan.injection

import org.hyperskill.app.study_plan.domain.interactor.StudyPlanInteractor
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository

interface StudyPlanDataComponent {
    val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository
    val studyPlanInteractor: StudyPlanInteractor
}