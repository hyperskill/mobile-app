package org.hyperskill.app.step_feedback.presentation

import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.Action
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.Message
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.ViewState
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class StepFeedbackViewModel(
    reduxViewContainer: ReduxViewContainer<ViewState, Message, Action.ViewAction>
) : ReduxViewModel<ViewState, Message, Action.ViewAction>(reduxViewContainer)