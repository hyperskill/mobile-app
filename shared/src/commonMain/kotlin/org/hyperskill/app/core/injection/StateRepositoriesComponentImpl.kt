package org.hyperskill.app.core.injection

import org.hyperskill.app.gamification_toolbar.data.repository.CurrentGamificationToolbarDataStateRepositoryImpl
import org.hyperskill.app.gamification_toolbar.domain.repository.CurrentGamificationToolbarDataStateRepository
import org.hyperskill.app.gamification_toolbar.remote.GamificationToolbarRemoteDataSourceImpl
import org.hyperskill.app.learning_activities.data.repository.NextLearningActivityStateRepositoryImpl
import org.hyperskill.app.learning_activities.domain.repository.NextLearningActivityStateRepository
import org.hyperskill.app.learning_activities.remote.LearningActivitiesRemoteDataSourceImpl
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.study_plan.data.repository.CurrentStudyPlanStateRepositoryImpl
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.study_plan.remote.StudyPlanRemoteDataSourceImpl
import org.hyperskill.app.subscriptions.cache.CurrentSubscriptionStateHolderImpl
import org.hyperskill.app.subscriptions.data.repository.CurrentSubscriptionStateRepositoryImpl
import org.hyperskill.app.subscriptions.data.source.CurrentSubscriptionStateHolder
import org.hyperskill.app.subscriptions.data.source.SubscriptionsRemoteDataSource
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.subscriptions.remote.SubscriptionsRemoteDataSourceImpl

class StateRepositoriesComponentImpl(appGraph: AppGraph) : StateRepositoriesComponent {

    private val authorizedHttpClient = appGraph.networkComponent.authorizedHttpClient

    private val featuresDataSource = appGraph.profileDataComponent.featuresDataSource

    private val purchaseInteractor: PurchaseInteractor = appGraph.buildPurchaseComponent().purchaseInteractor

    /**
     * Current subscription
     */
    private val subscriptionsRemoteDataSource: SubscriptionsRemoteDataSource =
        SubscriptionsRemoteDataSourceImpl(authorizedHttpClient)

    private val currentSubscriptionStateHolder: CurrentSubscriptionStateHolder by lazy {
        CurrentSubscriptionStateHolderImpl(
            json = appGraph.commonComponent.json,
            settings = appGraph.commonComponent.settings
        )
    }

    override val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository =
        CurrentSubscriptionStateRepositoryImpl(
            subscriptionsRemoteDataSource = subscriptionsRemoteDataSource,
            stateHolder = currentSubscriptionStateHolder,
            featuresDataSource = featuresDataSource,
            purchaseInteractor = purchaseInteractor
        )

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

    /**
     * Gamification toolbar
     */
    override val currentGamificationToolbarDataStateRepository: CurrentGamificationToolbarDataStateRepository by lazy {
        CurrentGamificationToolbarDataStateRepositoryImpl(
            GamificationToolbarRemoteDataSourceImpl(authorizedHttpClient)
        )
    }
}