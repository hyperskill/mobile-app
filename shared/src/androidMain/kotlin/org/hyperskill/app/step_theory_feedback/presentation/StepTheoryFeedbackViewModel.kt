package org.hyperskill.app.step_theory_feedback.presentation

import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.Action
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.Message
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackFeature.ViewState
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class StepTheoryFeedbackViewModel(
    reduxViewContainer: ReduxViewContainer<ViewState, Message, Action.ViewAction>
) : ReduxViewModel<ViewState, Message, Action.ViewAction>(reduxViewContainer)