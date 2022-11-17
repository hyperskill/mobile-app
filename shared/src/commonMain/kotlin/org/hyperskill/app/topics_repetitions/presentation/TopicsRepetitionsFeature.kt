package org.hyperskill.app.topics_repetitions.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.topics_repetitions.domain.model.TopicsRepetitions

interface TopicsRepetitionsFeature {
    sealed interface State {
        object Idle : State
        object Loading : State

        data class Content(
            val topicsRepetitions: TopicsRepetitions
        ) : State

        object NetworkError : State
    }

    sealed interface Message {

        data class Initialize(val forceUpdate: Boolean) : Message

        /**
         * Analytic
         */
    }

    sealed interface Action {

        object Initialize : Action

        object FetchNextTopics : Action

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
        }
    }
}