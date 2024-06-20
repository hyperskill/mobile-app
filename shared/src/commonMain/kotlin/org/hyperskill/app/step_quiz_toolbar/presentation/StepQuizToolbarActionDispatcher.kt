package org.hyperskill.app.step_quiz_toolbar.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.SingleAnalyticEventActionDispatcher
import org.hyperskill.app.core.presentation.CompositeActionDispatcher

class StepQuizToolbarActionDispatcher internal constructor(
    mainStepQuizToolbarActionDispatcher: MainStepQuizToolbarActionDispatcher,
    analyticInteractor: AnalyticInteractor
) : CompositeActionDispatcher<StepQuizToolbarFeature.Action, StepQuizToolbarFeature.Message>(
    listOf(
        mainStepQuizToolbarActionDispatcher,
        SingleAnalyticEventActionDispatcher(analyticInteractor) {
            (it as? StepQuizToolbarFeature.InternalAction.LogAnalyticEvent)?.event
        }
    )
)