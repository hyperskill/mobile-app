package org.hyperskill.app.step_toolbar.presentation

import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.Action
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.InternalAction
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.Message
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class StepToolbarReducer(
    private val stepRoute: StepRoute
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is StepToolbarFeature.InternalMessage.Initialize ->
                if (message.topicId != null) {
                    State.Loading to setOf(InternalAction.FetchTopicProgress(message.topicId))
                } else {
                    State.Idle to emptySet()
                }
            StepToolbarFeature.InternalMessage.FetchTopicProgressError ->
                State.Error to emptySet()
            is StepToolbarFeature.InternalMessage.FetchTopicProgressSuccess ->
                State.Content(message.topicProgress) to emptySet()
        }
}