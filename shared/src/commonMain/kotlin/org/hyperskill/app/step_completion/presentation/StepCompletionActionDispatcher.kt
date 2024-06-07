package org.hyperskill.app.step_completion.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.SingleAnalyticEventActionDispatcher
import org.hyperskill.app.core.presentation.CompositeActionDispatcher
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Action
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Message

class StepCompletionActionDispatcher internal constructor(
    mainStepCompletionActionDispatcher: MainStepCompletionActionDispatcher,
    analyticInteractor: AnalyticInteractor
) : CompositeActionDispatcher<Action, Message>(
    listOf(
        mainStepCompletionActionDispatcher,
        SingleAnalyticEventActionDispatcher(analyticInteractor) {
            (it as? StepCompletionFeature.InternalAction.LogAnalyticEvent)?.analyticEvent
        }
    )
)