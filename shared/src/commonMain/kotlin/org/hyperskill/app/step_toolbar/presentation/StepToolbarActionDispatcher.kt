package org.hyperskill.app.step_toolbar.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.SingleAnalyticEventActionDispatcher
import org.hyperskill.app.core.presentation.CompositeActionDispatcher

class StepToolbarActionDispatcher internal constructor(
    mainStepToolbarActionDispatcher: MainStepToolbarActionDispatcher,
    analyticInteractor: AnalyticInteractor
) : CompositeActionDispatcher<StepToolbarFeature.Action, StepToolbarFeature.Message>(
    listOf(
        mainStepToolbarActionDispatcher,
        SingleAnalyticEventActionDispatcher(analyticInteractor) {
            (it as? StepToolbarFeature.InternalAction.LogAnalyticEvent)?.event
        }
    )
)