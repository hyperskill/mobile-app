package org.hyperskill.app.streak_recovery.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.SingleAnalyticEventActionDispatcher
import org.hyperskill.app.core.presentation.CompositeActionDispatcher
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature.Message

class StreakRecoveryActionDispatcher internal constructor(
    mainStreakRecoveryActionDispatcher: MainStreakRecoveryActionDispatcher,
    analyticInteractor: AnalyticInteractor
) : CompositeActionDispatcher<StreakRecoveryFeature.Action, Message>(
    listOf(
        mainStreakRecoveryActionDispatcher,
        SingleAnalyticEventActionDispatcher(analyticInteractor) {
            (it as? StreakRecoveryFeature.InternalAction.LogAnalyticEvent)?.event
        }
    )
)