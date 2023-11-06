package org.hyperskill.app.gamification_toolbar.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.gamification_toolbar.domain.repository.CurrentGamificationToolbarDataStateRepository

class GamificationToolbarDataComponentImpl(private val appGraph: AppGraph) : GamificationToolbarDataComponent {
    override val currentGamificationToolbarDataStateRepository: CurrentGamificationToolbarDataStateRepository
        get() = appGraph.stateRepositoriesComponent.currentGamificationToolbarDataStateRepository
}