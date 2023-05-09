package org.hyperskill.app.step_quiz_hints.presentation

import org.hyperskill.app.step.domain.model.Step
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class StepQuizHintsViewModel(
    reduxViewContainer: ReduxViewContainer<
        StepQuizHintsFeature.ViewState, StepQuizHintsFeature.Message, StepQuizHintsFeature.Action.ViewAction>,
    step: Step
) : ReduxViewModel<
    StepQuizHintsFeature.ViewState,
    StepQuizHintsFeature.Message,
    StepQuizHintsFeature.Action.ViewAction>(reduxViewContainer) {
    init {
        if (StepQuizHintsFeature.isHintsFeatureAvailable(step)) {
            onNewMessage(
                StepQuizHintsFeature.Message.InitWithStepId(step.id)
            )
        }
    }
}