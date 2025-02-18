package org.hyperskill.app.step_quiz_hints.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.SingleAnalyticEventActionDispatcher
import org.hyperskill.app.core.presentation.CompositeActionDispatcher
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Action
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.InternalAction
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Message

class StepQuizHintsActionDispatcher internal constructor(
    mainStepQuizHintsActionDispatcher: MainStepQuizHintsActionDispatcher,
    analyticInteractor: AnalyticInteractor
) : CompositeActionDispatcher<Action, Message>(
    listOf(
        mainStepQuizHintsActionDispatcher,
        SingleAnalyticEventActionDispatcher(analyticInteractor) {
            (it as? InternalAction.LogAnalyticEvent)?.analyticEvent
        }
    )
)