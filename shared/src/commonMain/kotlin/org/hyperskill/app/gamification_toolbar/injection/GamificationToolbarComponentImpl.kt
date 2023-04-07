package org.hyperskill.app.gamification_toolbar.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarActionDispatcher
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer

class GamificationToolbarComponentImpl(private val appGraph: AppGraph) : GamificationToolbarComponent {
    override val gamificationToolbarReducer: GamificationToolbarReducer
        get() = GamificationToolbarReducer()

    override val gamificationToolbarActionDispatcher: GamificationToolbarActionDispatcher
        get() = GamificationToolbarActionDispatcher(
            ActionDispatcherOptions(),
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.buildStreaksDataComponent().streaksInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.streakFlowDataComponent.streakFlow,
            appGraph.stateRepositoriesComponent.currentStudyPlanStateRepository,
            appGraph.buildTrackDataComponent().trackRepository,
            appGraph.buildProgressesDataComponent().progressesRepository
        )
}