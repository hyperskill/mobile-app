package org.hyperskill.app.theory_feedback.presentation

import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.Action
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.Message
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.ViewState
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class TheoryFeedbackViewModel(
    reduxViewContainer: ReduxViewContainer<ViewState, Message, Action.ViewAction>
) : ReduxViewModel<ViewState, Message, Action.ViewAction>(reduxViewContainer)