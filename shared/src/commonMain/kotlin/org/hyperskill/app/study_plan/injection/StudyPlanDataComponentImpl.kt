package org.hyperskill.app.study_plan.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.study_plan.domain.interactor.StudyPlanInteractor
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository

class StudyPlanDataComponentImpl(private val appGraph: AppGraph) : StudyPlanDataComponent {

    override val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository
        get() = appGraph.stateRepositoriesComponent.studyPlanStateRepository

    override val studyPlanInteractor: StudyPlanInteractor
        get() = StudyPlanInteractor(currentStudyPlanStateRepository)
}