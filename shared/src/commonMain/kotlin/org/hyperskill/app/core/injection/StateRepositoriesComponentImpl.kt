package org.hyperskill.app.core.injection

import org.hyperskill.app.gamification_toolbar.data.repository.CurrentGamificationToolbarDataStateRepositoryImpl
import org.hyperskill.app.gamification_toolbar.domain.repository.CurrentGamificationToolbarDataStateRepository
import org.hyperskill.app.gamification_toolbar.remote.GamificationToolbarRemoteDataSourceImpl
import org.hyperskill.app.interview_steps.data.repository.InterviewStepsStateRepositoryImpl
import org.hyperskill.app.interview_steps.domain.repository.InterviewStepsStateRepository
import org.hyperskill.app.interview_steps.remote.TrackInterviewStepsRemoteDataSourceImpl
import org.hyperskill.app.learning_activities.data.repository.NextLearningActivityStateRepositoryImpl
import org.hyperskill.app.learning_activities.domain.repository.NextLearningActivityStateRepository
import org.hyperskill.app.learning_activities.remote.LearningActivitiesRemoteDataSourceImpl
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

    /**
     * Current subscription
     */
    private val subscriptionsRemoteDataSource: SubscriptionsRemoteDataSource =
        SubscriptionsRemoteDataSourceImpl(authorizedHttpClient)

    private val currentSubscriptionStateHolder: CurrentSubscriptionStateHolder =
        CurrentSubscriptionStateHolderImpl(
            json = appGraph.commonComponent.json,
            settings = appGraph.commonComponent.settings
        )

    override val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository =
        CurrentSubscriptionStateRepositoryImpl(subscriptionsRemoteDataSource, currentSubscriptionStateHolder)

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

    override val interviewStepsStateRepository: InterviewStepsStateRepository by lazy {
        InterviewStepsStateRepositoryImpl(TrackInterviewStepsRemoteDataSourceImpl(authorizedHttpClient))
    }
}