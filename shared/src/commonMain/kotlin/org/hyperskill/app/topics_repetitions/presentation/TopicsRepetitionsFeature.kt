package org.hyperskill.app.topics_repetitions.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetition
import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetitionStatistics
import kotlin.math.ceil

interface TopicsRepetitionsFeature {
    sealed interface State {
        object Idle : State
        object Loading : State

        data class Content(
            val topicsRepetitions: List<TopicRepetition>,
            val topicRepetitionStatistics: TopicRepetitionStatistics,
            val trackTitle: String,
            val isLoadingNextTopics: Boolean = false
        ) : State {
            val hasNextTopicsToLoad: Boolean
                get() = topicRepetitionStatistics.totalCount > topicsRepetitions.count()
            val currentPage: Int
                get() = ceil(
                    topicsRepetitions.count().toFloat() / TopicsRepetitionsActionDispatcher.TOPICS_PAGINATION_SIZE
                ).toInt()
        }

        object NetworkError : State
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean) : Message

        sealed interface TopicsRepetitionsLoaded : Message {
            data class Success(
                val topicsRepetitions: List<TopicRepetition>,
                val topicRepetitionStatistics: TopicRepetitionStatistics,
                val trackTitle: String
            ) : TopicsRepetitionsLoaded

            object Error : TopicsRepetitionsLoaded
        }

        object ShowMoreButtonClicked : Message

        sealed interface NextTopicsRepetitionsLoaded : Message {
            data class Success(
                val nextTopicsRepetitions: List<TopicRepetition>,
                val nextPage: Int
            ) : NextTopicsRepetitionsLoaded

            object Error : NextTopicsRepetitionsLoaded
        }

        data class StepCompleted(val stepId: Long) : Message

        data class RepeatTopicClicked(val topicId: Long) : Message
        object RepeatNextTopicClicked : Message

        object ViewedEventMessage : Message
    }

    sealed interface Action {
        object Initialize : Action

        data class FetchNextTopics(val nextPage: Int) : Action

        data class NotifyTopicRepeated(val topicId: Long) : Action

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
                data class StepScreen(val stepId: Long) : NavigateTo
            }
        }
    }
}