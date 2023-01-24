package org.hyperskill.app.topics_to_discover_next.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransaction
import org.hyperskill.app.topics.domain.model.Topic
import org.hyperskill.app.topics_to_discover_next.domain.model.TopicsToDiscoverNextScreen

interface TopicsToDiscoverNextFeature {
    sealed interface State {
        /**
         * Represents initial state.
         */
        object Idle : State

        /**
         * Represents a state when loading topics to discover next.
         */
        object Loading : State

        /**
         * Represents a state when topics to discover next failed to load.
         */
        object Error : State

        /**
         * Represents a state when topics to discover next is empty.
         */
        object Empty : State

        /**
         * Represents a state when topics to discover next successfully loaded.
         *
         * @property topicsToDiscoverNext Current user profile uncompleted topics (theory to discover next) for current stage.
         * @property isRefreshing A boolean flag that indicates about is pull-to-refresh is ongoing.
         *
         * @see Topic
         */
        data class Content(
            val topicsToDiscoverNext: List<Topic>,
            internal val isRefreshing: Boolean = false
        ) : State
    }

    sealed interface Message {
        /**
         * Initialization
         */
        data class Initialize(val screen: TopicsToDiscoverNextScreen, val forceUpdate: Boolean = false) : Message
        object FetchTopicsToDiscoverNextError : Message
        data class FetchTopicsToDiscoverNextSuccess(val topicsToDiscoverNext: List<Topic>) : Message

        data class PullToRefresh(val screen: TopicsToDiscoverNextScreen) : Message

        /**
         * A message that indicates about click on topic.
         * Triggers navigation to step screen and logs that event to the analytics.
         *
         * @property topicId A topic id that triggered that event.
         */
        data class TopicToDiscoverNextClicked(val topicId: Long, val screen: TopicsToDiscoverNextScreen) : Message
    }

    sealed interface Action {
        data class FetchTopicsToDiscoverNext(val sentryTransaction: HyperskillSentryTransaction) : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed interface ViewAction : Action {
            data class ShowStepScreen(val stepId: Long) : ViewAction
        }
    }
}