package org.hyperskill.app.core.injection

import org.hyperskill.app.learning_activities.data.repository.NextLearningActivityStateRepositoryImpl
import org.hyperskill.app.learning_activities.domain.repository.NextLearningActivityStateRepository
import org.hyperskill.app.learning_activities.remote.LearningActivitiesRemoteDataSourceImpl
import org.hyperskill.app.study_plan.data.repository.CurrentStudyPlanStateRepositoryImpl
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.study_plan.remote.StudyPlanRemoteDataSourceImpl
import org.hyperskill.app.subscriptions.data.repository.CurrentSubscriptionStateRepositoryImpl
import org.hyperskill.app.subscriptions.data.source.SubscriptionsRemoteDataSource
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.subscriptions.remote.SubscriptionsRemoteDataSourceImpl

class StateRepositoriesComponentImpl(appGraph: AppGraph) : StateRepositoriesComponent {

    private val authorizedHttpClient = appGraph.networkComponent.authorizedHttpClient

    /**
     * Current subscription
     */
    private val subscriptionsRemoteDataSource: SubscriptionsRemoteDataSource =
        SubscriptionsRemoteDataSourceImpl(authorizedHttpClient)

    override val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository =
        CurrentSubscriptionStateRepositoryImpl(subscriptionsRemoteDataSource)

    /**
     * Study plan
     */
    override val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository by lazy {
        CurrentStudyPlanStateRepositoryImpl(StudyPlanRemoteDataSourceImpl(authorizedHttpClient))
    }

    /**
     * Learning activities
     */
    override val nextLearningActivityStateRepository: NextLearningActivityStateRepository by lazy {
        NextLearningActivityStateRepositoryImpl(LearningActivitiesRemoteDataSourceImpl(authorizedHttpClient))
    }
}