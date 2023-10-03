package org.hyperskill.app.step_quiz.presentation

import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Action
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Message
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.State
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer
import ru.nobird.app.presentation.redux.feature.Feature

class StepQuizViewModel(
    private val feature: Feature<State, Message, Action>
) : ReduxViewModel<State, Message, Action.ViewAction>(feature.wrapWithViewContainer()) {

    fun syncReply(reply: Reply) {
        if (StepQuizResolver.shouldSyncReply(feature.state.stepQuizState)) {
            onNewMessage(Message.SyncReply(reply))
        }
    }
}