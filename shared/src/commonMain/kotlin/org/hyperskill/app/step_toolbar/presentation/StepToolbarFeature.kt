package org.hyperskill.app.step_toolbar.presentation

import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.topics.domain.model.TopicProgress

object StepToolbarFeature {
    sealed interface State {
        object Idle : State
        object Unavailable : State
        object Loading : State
        object Error : State
        data class Content(
            val topicProgress: TopicProgress
        ) : State
    }

    internal fun initialState(stepRoute: StepRoute): State =
        if (StepToolbarResolver.isProgressBarAvailable(stepRoute)) {
            State.Idle
        } else {
            State.Unavailable
        }

    sealed interface ViewState {
        object Idle : ViewState
        object Unavailable : ViewState
        object Loading : ViewState
        object Error : ViewState
        data class Content(
            val progress: Float // a value between 0 and 1
        ) : ViewState
    }

    sealed interface Message

    internal sealed interface InternalMessage : Message {
        data class Initialize(
            val topicId: Long?,
            val forceUpdate: Boolean = false
        ) : InternalMessage

        object FetchTopicProgressError : InternalMessage
        data class FetchTopicProgressSuccess(val topicProgress: TopicProgress) : InternalMessage

        data class TopicCompleted(val topicId: Long) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data class FetchTopicProgress(
            val topicId: Long,
            val forceLoadFromRemote: Boolean
        ) : InternalAction
    }
}