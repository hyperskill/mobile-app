package org.hyperskill.app.topics_repetitions.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.topics_repetitions.domain.model.TopicsRepetitions
import org.hyperskill.app.topics_repetitions.view.model.TopicToRepeat

interface TopicsRepetitionsFeature {
    sealed interface State {
        object Idle : State
        object Loading : State

        data class Content(
            val topicsRepetitions: TopicsRepetitions,
            val topicsToRepeat: List<TopicToRepeat>,
            val recommendedTopicsToRepeatCount: Int,
            val trackTitle: String,
            val nextTopicsLoading: Boolean = false
        ) : State

        object NetworkError : State
    }

    sealed interface Message {

        data class Initialize(val forceUpdate: Boolean) : Message

        sealed interface TopicsRepetitionsLoaded : Message {
            data class Success(
                val topicsRepetitions: TopicsRepetitions,
                val topicsToRepeat: List<TopicToRepeat>,
                val recommendedTopicsToRepeatCount: Int,
                val trackTitle: String,
            ) : TopicsRepetitionsLoaded

            object Error : TopicsRepetitionsLoaded
        }

        object ShowMoreButtonClicked : Message

        sealed interface NextTopicsLoaded : Message {
            data class Success(
                val remainingTopicsRepetitions: TopicsRepetitions,
                val nextTopicsToRepeat: List<TopicToRepeat>
            ) : NextTopicsLoaded

            object Error : NextTopicsLoaded
        }

        data class TopicRepeated(val topicId: Long) : Message

        /**
         * Analytic
         */
        object ClickedRepeatNextTopicEventMessage : Message
        object ClickedRepeatTopicEventMessage : Message
    }

    sealed interface Action {

        object Initialize : Action

        data class FetchNextTopics(val topicsRepetitions: TopicsRepetitions) : Action

        object UpdateCurrentProfile : Action
        /**
         * Logging analytic event action
         *
         * @property analyticEvent event to be logged
         */
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed interface ViewAction : Action {
            /**
             * Shows snackbar with error message
             */
            object ShowNetworkError : ViewAction

            sealed interface NavigateTo : ViewAction {
                data class StepScreen(val stepId: Long, val topicId: Long) : NavigateTo
            }
        }
    }
}