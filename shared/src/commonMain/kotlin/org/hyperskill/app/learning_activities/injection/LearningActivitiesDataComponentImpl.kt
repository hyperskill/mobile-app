package org.hyperskill.app.learning_activities.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.learning_activities.data.repository.LearningActivitiesRepositoryImpl
import org.hyperskill.app.learning_activities.data.source.LearningActivitiesRemoteDataSource
import org.hyperskill.app.learning_activities.domain.interactor.LearningActivitiesInteractor
import org.hyperskill.app.learning_activities.domain.repository.LearningActivitiesRepository
import org.hyperskill.app.learning_activities.remote.LearningActivitiesRemoteDataSourceImpl

class LearningActivitiesDataComponentImpl(
    appGraph: AppGraph
) : LearningActivitiesDataComponent {
    private val learningActivitiesRemoteDataSource: LearningActivitiesRemoteDataSource =
        LearningActivitiesRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    private val learningActivitiesRepository: LearningActivitiesRepository =
        LearningActivitiesRepositoryImpl(learningActivitiesRemoteDataSource)

    override val learningActivitiesInteractor: LearningActivitiesInteractor
        get() = LearningActivitiesInteractor(learningActivitiesRepository)
}