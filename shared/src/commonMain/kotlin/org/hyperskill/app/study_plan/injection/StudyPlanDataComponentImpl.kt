package org.hyperskill.app.study_plan.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.learning_activities.data.repository.LearningActivitiesRepositoryImpl
import org.hyperskill.app.learning_activities.domain.repository.LearningActivitiesRepository
import org.hyperskill.app.learning_activities.remote.LearningActivitiesRemoteDataSourceImpl
import org.hyperskill.app.study_plan.data.repository.StudyPlanSectionsRepositoryImpl
import org.hyperskill.app.study_plan.domain.interactor.StudyPlanInteractor
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.study_plan.domain.repository.StudyPlanSectionsRepository
import org.hyperskill.app.study_plan.remote.StudyPlanSectionsRemoteDataSourceImpl

class StudyPlanDataComponentImpl(private val appGraph: AppGraph) : StudyPlanDataComponent {

    private val studyPlanSectionsRemoteDataSource = StudyPlanSectionsRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )

    private val learningActivitiesRemoteDataSource = LearningActivitiesRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )

    override val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository
        get() = appGraph.stateRepositoriesComponent.currentStudyPlanStateRepository

    override val studyPlanSectionsRepository: StudyPlanSectionsRepository
        get() = StudyPlanSectionsRepositoryImpl(studyPlanSectionsRemoteDataSource)

    override val learningActivitiesRepository: LearningActivitiesRepository
        get() = LearningActivitiesRepositoryImpl(learningActivitiesRemoteDataSource)

    override val studyPlanInteractor: StudyPlanInteractor
        get() = StudyPlanInteractor(
            currentStudyPlanStateRepository,
            studyPlanSectionsRepository,
            learningActivitiesRepository
        )
}