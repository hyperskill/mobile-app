package org.hyperskill.app.step_quiz.presentation

import org.hyperskill.app.step.domain.model.Step
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class StepQuizViewModel(
    reduxViewContainer: ReduxViewContainer<StepQuizFeature.State, StepQuizFeature.Message, StepQuizFeature.Action.ViewAction>,
    private val step: Step
) : ReduxViewModel<StepQuizFeature.State, StepQuizFeature.Message, StepQuizFeature.Action.ViewAction>(reduxViewContainer) {
    init {
        onNewMessage(StepQuizFeature.Message.InitWithStep(step))
    }

    fun onRetryButtonClicked() {
        onNewMessage(StepQuizFeature.Message.ClickedRetryEventMessage)
        onNewMessage(
            StepQuizFeature.Message.CreateAttemptClicked(
                step = step,
                shouldResetReply = true
            )
        )
    }
}