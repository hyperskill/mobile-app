package org.hyperskill.app.gamification_toolbar.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarActionDispatcher
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer

class GamificationToolbarComponentImpl(
    private val appGraph: AppGraph,
    private val screen: GamificationToolbarScreen
) : GamificationToolbarComponent {
    override val gamificationToolbarReducer: GamificationToolbarReducer
        get() = GamificationToolbarReducer(screen)

    override val gamificationToolbarActionDispatcher: GamificationToolbarActionDispatcher
        get() = GamificationToolbarActionDispatcher(
            config = ActionDispatcherOptions(),
            submissionRepository = appGraph.submissionDataComponent.submissionRepository,
            streakFlow = appGraph.streakFlowDataComponent.streakFlow,
            currentStudyPlanStateRepository = appGraph.stateRepositoriesComponent.currentStudyPlanStateRepository,
            topicCompletedFlow = appGraph.stepCompletionFlowDataComponent.topicCompletedFlow,
            currentGamificationToolbarDataStateRepository = appGraph.stateRepositoriesComponent
                .currentGamificationToolbarDataStateRepository,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor
        )
}