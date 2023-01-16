package org.hyperskill.app.gamification_toolbar.injection

import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarActionDispatcher
import org.hyperskill.app.gamification_toolbar.presentation.reducer.GamificationToolbarReducer

interface GamificationToolbarComponent {
    val gamificationToolbarReducer: GamificationToolbarReducer
    val gamificationToolbarActionDispatcher: GamificationToolbarActionDispatcher
}