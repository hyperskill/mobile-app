package org.hyperskill.app.step_quiz_hints.presentation

import org.hyperskill.app.step.domain.model.CommentThread
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz_hints.model.StepQuizHintsViewState
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class StepQuizHintsViewModel(
    reduxViewContainer: ReduxViewContainer<StepQuizHintsViewState, StepQuizHintsFeature.Message, StepQuizHintsFeature.Action.ViewAction>,
    step: Step
) : ReduxViewModel<StepQuizHintsViewState, StepQuizHintsFeature.Message, StepQuizHintsFeature.Action.ViewAction>(reduxViewContainer) {
    init {
        if (step.commentsStatistics.any { it.thread == CommentThread.HINT && it.totalCount > 0 }) {
            onNewMessage(
                StepQuizHintsFeature.Message.InitWithStepId(step.id)
            )
        }
    }
}