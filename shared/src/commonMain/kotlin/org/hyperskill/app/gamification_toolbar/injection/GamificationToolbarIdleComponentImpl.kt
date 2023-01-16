package org.hyperskill.app.gamification_toolbar.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarActionDispatcher
import org.hyperskill.app.gamification_toolbar.presentation.reducer.GamificationToolbarIdleReducer
import org.hyperskill.app.gamification_toolbar.presentation.reducer.GamificationToolbarReducer

// TODO: ALTAPPS-519 delete this class
class GamificationToolbarIdleComponentImpl(private val appGraph: AppGraph) : GamificationToolbarComponent {
    override val gamificationToolbarReducer: GamificationToolbarReducer
        get() = GamificationToolbarIdleReducer()

    override val gamificationToolbarActionDispatcher: GamificationToolbarActionDispatcher
        get() = GamificationToolbarActionDispatcher(
            ActionDispatcherOptions(),
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.buildStreaksDataComponent().streaksInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor
        )
}