package org.hyperskill.app.study_plan.injection

import org.hyperskill.app.study_plan.domain.interactor.StudyPlanInteractor

interface StudyPlanDataComponent {
    val studyPlanInteractor: StudyPlanInteractor
}