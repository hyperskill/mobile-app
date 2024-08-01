package org.hyperskill.app.gamification_toolbar.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarActionDispatcher
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.gamification_toolbar.presentation.MainGamificationToolbarActionDispatcher

internal class GamificationToolbarComponentImpl(
    private val appGraph: AppGraph,
    private val screen: GamificationToolbarScreen
) : GamificationToolbarComponent {

    override val gamificationToolbarReducer: GamificationToolbarReducer
        get() = GamificationToolbarReducer(screen)

    private val mainGamificationToolbarActionDispatcher: MainGamificationToolbarActionDispatcher
        get() = MainGamificationToolbarActionDispatcher(
            config = ActionDispatcherOptions(),
            stepCompletedFlow = appGraph.stepCompletionFlowDataComponent.stepCompletedFlow,
            streakFlow = appGraph.streakFlowDataComponent.streakFlow,
            currentStudyPlanStateRepository = appGraph.stateRepositoriesComponent.currentStudyPlanStateRepository,
            topicCompletedFlow = appGraph.stepCompletionFlowDataComponent.topicCompletedFlow,
            currentGamificationToolbarDataStateRepository = appGraph.stateRepositoriesComponent
                .currentGamificationToolbarDataStateRepository,
            subscriptionInteractor = appGraph.subscriptionDataComponent.subscriptionsInteractor,
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor
        )

    override val gamificationToolbarActionDispatcher: GamificationToolbarActionDispatcher
        get() = GamificationToolbarActionDispatcher(
            mainGamificationToolbarActionDispatcher,
            appGraph.analyticComponent.analyticInteractor
        )
}