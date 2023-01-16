package org.hyperskill.app.gamification_toolbar.presentation.reducer

import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import ru.nobird.app.presentation.redux.reducer.StateReducer

// TODO: ALTAPPS-519 delete this interface
interface GamificationToolbarReducer :
    StateReducer<GamificationToolbarFeature.State, GamificationToolbarFeature.Message, GamificationToolbarFeature.Action>