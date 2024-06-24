package org.hyperskill.app.challenges.widget.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.SingleAnalyticEventActionDispatcher
import org.hyperskill.app.core.presentation.CompositeActionDispatcher

class ChallengeWidgetActionDispatcher internal constructor(
    mainChallengeWidgetActionDispatcher: MainChallengeWidgetActionDispatcher,
    analyticInteractor: AnalyticInteractor
) : CompositeActionDispatcher<ChallengeWidgetFeature.Action, ChallengeWidgetFeature.Message>(
    listOf(
        mainChallengeWidgetActionDispatcher,
        SingleAnalyticEventActionDispatcher(analyticInteractor) {
            (it as? ChallengeWidgetFeature.InternalAction.LogAnalyticEvent)?.analyticEvent
        }
    )
)