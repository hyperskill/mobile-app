package org.hyperskill.app.streak_recovery.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryActionDispatcher
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryReducer

class StreakRecoveryComponentImpl(
    private val appGraph: AppGraph
) : StreakRecoveryComponent {
    override val streakRecoveryReducer: StreakRecoveryReducer
        get() = StreakRecoveryReducer()

    override val streakRecoveryActionDispatcher: StreakRecoveryActionDispatcher
        get() = StreakRecoveryActionDispatcher(
            ActionDispatcherOptions(),
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.buildStreaksDataComponent().streaksInteractor,
            appGraph.analyticComponent.analyticInteractor
        )
}