package org.hyperskill.app.gamification_toolbar.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.SingleAnalyticEventActionDispatcher
import org.hyperskill.app.core.presentation.CompositeActionDispatcher
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Action
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Message

class GamificationToolbarActionDispatcher internal constructor(
    mainGamificationToolbarActionDispatcher: MainGamificationToolbarActionDispatcher,
    analyticInteractor: AnalyticInteractor
) : CompositeActionDispatcher<Action, Message>(
    listOf(
        mainGamificationToolbarActionDispatcher,
        SingleAnalyticEventActionDispatcher(analyticInteractor) {
            (it as? GamificationToolbarFeature.InternalAction.LogAnalyticEvent)?.analyticEvent
        }
    )
)