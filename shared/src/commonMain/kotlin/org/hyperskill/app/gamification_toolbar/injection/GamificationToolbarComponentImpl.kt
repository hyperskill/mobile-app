package org.hyperskill.app.gamification_toolbar.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.gamification_toolbar.data.repository.GamificationToolbarRepositoryImpl
import org.hyperskill.app.gamification_toolbar.data.source.GamificationToolbarRemoteDataSource
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.domain.repository.GamificationToolbarRepository
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarActionDispatcher
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.gamification_toolbar.remote.GamificationToolbarRemoteDataSourceImpl

class GamificationToolbarComponentImpl(
    private val appGraph: AppGraph,
    private val screen: GamificationToolbarScreen
) : GamificationToolbarComponent {
    override val gamificationToolbarReducer: GamificationToolbarReducer
        get() = GamificationToolbarReducer(screen)

    private val gamificationToolbarRemoteDataSource: GamificationToolbarRemoteDataSource =
        GamificationToolbarRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    private val gamificationToolbarRepository: GamificationToolbarRepository
        get() = GamificationToolbarRepositoryImpl(gamificationToolbarRemoteDataSource)

    override val gamificationToolbarActionDispatcher: GamificationToolbarActionDispatcher
        get() {
            val profileComponent = appGraph.profileDataComponent
            return GamificationToolbarActionDispatcher(
                ActionDispatcherOptions(),
                appGraph.submissionDataComponent.submissionRepository,
                appGraph.streakFlowDataComponent.streakFlow,
                profileComponent.currentProfileStateRepository,
                appGraph.stateRepositoriesComponent.currentStudyPlanStateRepository,
                appGraph.stepCompletionFlowDataComponent.topicCompletedFlow,
                gamificationToolbarRepository,
                appGraph.analyticComponent.analyticInteractor,
                appGraph.sentryComponent.sentryInteractor
            )
        }
}