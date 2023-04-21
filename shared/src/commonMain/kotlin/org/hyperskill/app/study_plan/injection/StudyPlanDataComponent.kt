package org.hyperskill.app.study_plan.injection

import org.hyperskill.app.learning_activities.domain.repository.LearningActivitiesRepository
import org.hyperskill.app.study_plan.domain.interactor.StudyPlanInteractor
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.study_plan.domain.repository.StudyPlanSectionsRepository

interface StudyPlanDataComponent {
    val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository
    val studyPlanSectionsRepository: StudyPlanSectionsRepository
    val learningActivitiesRepository: LearningActivitiesRepository
    val studyPlanInteractor: StudyPlanInteractor
}