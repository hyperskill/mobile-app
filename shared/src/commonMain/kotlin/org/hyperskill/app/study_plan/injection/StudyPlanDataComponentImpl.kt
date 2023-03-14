package org.hyperskill.app.study_plan.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.study_plan.data.repository.StudyPlanRepositoryImpl
import org.hyperskill.app.study_plan.data.repository.StudyPlanSectionsRepositoryImpl
import org.hyperskill.app.study_plan.data.source.StudyPlanRemoteDataSource
import org.hyperskill.app.study_plan.domain.interactor.StudyPlanInteractor
import org.hyperskill.app.study_plan.domain.repository.StudyPlanRepository
import org.hyperskill.app.study_plan.domain.repository.StudyPlanSectionsRepository
import org.hyperskill.app.study_plan.remote.StudyPlanRemoteDataSourceImpl
import org.hyperskill.app.study_plan.remote.StudyPlanSectionsRemoteDataSourceImpl

class StudyPlanDataComponentImpl(appGraph: AppGraph) : StudyPlanDataComponent {
    private val studyPlanRemoteDataSource: StudyPlanRemoteDataSource = StudyPlanRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )

    private val studyPlanSectionsRemoteDataSource = StudyPlanSectionsRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )

    override val studyPlanRepository: StudyPlanRepository
        get() = StudyPlanRepositoryImpl(studyPlanRemoteDataSource)

    override val studyPlanSectionsRepository: StudyPlanSectionsRepository
        get() = StudyPlanSectionsRepositoryImpl(studyPlanSectionsRemoteDataSource)

    override val studyPlanInteractor: StudyPlanInteractor
        get() = StudyPlanInteractor(studyPlanRepository, studyPlanSectionsRepository)
}