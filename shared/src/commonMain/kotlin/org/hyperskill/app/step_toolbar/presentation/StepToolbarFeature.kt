package org.hyperskill.app.step_toolbar.presentation

import org.hyperskill.app.topics.domain.model.TopicProgress

object StepToolbarFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(
            val topicProgress: TopicProgress
        ) : State
    }

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        object Error : ViewState
        data class Content(
            val progress: Float // a value between 0 and 1
        ) : ViewState
    }

    sealed interface Message

    internal sealed interface InternalMessage : Message {
        data class Initialize(val topicId: Long?) : InternalMessage

        object FetchTopicProgressError : InternalMessage
        data class FetchTopicProgressSuccess(val topicProgress: TopicProgress) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data class FetchTopicProgress(val topicId: Long) : InternalAction
    }
}