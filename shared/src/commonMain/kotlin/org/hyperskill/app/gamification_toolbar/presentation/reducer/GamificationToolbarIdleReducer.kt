package org.hyperskill.app.gamification_toolbar.presentation.reducer

import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Action
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Message
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.State

// TODO: ALTAPPS-519 delete this class
class GamificationToolbarIdleReducer : GamificationToolbarReducer {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        State.Idle to emptySet()
}