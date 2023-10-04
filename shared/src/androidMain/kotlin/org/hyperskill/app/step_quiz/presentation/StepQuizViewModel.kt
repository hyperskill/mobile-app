package org.hyperskill.app.step_quiz.presentation

import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class StepQuizViewModel(
    reduxViewContainer: ReduxViewContainer<
        StepQuizFeature.State, StepQuizFeature.Message, StepQuizFeature.Action.ViewAction>
) : ReduxViewModel<
    StepQuizFeature.State, StepQuizFeature.Message, StepQuizFeature.Action.ViewAction>(reduxViewContainer)