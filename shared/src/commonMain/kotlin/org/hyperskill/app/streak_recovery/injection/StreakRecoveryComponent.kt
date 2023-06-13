package org.hyperskill.app.streak_recovery.injection

import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryActionDispatcher
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryReducer

interface StreakRecoveryComponent {
    val streakRecoveryReducer: StreakRecoveryReducer
    val streakRecoveryActionDispatcher: StreakRecoveryActionDispatcher
}