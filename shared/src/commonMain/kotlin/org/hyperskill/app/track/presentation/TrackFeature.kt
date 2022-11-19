package org.hyperskill.app.track.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.topics.domain.model.Topic
import org.hyperskill.app.track.domain.model.StudyPlan
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackProgress

interface TrackFeature {
    sealed interface State {
        /**
         * Represents initial state.
         */
        object Idle : State

        /**
         * Represents a state when loading track screen data.
         */
        object Loading : State

        /**
         * Represents a state when track screen data successfully loaded.
         *
         * @property track Current user profile selected track.
         * @property trackProgress Current user profile selected track progress.
         * @property studyPlan Current user profile study plan.
         * @property topicsToLearn Current user profile uncompleted topics (theory to discover next) for current stage.
         * @property isRefreshing A boolean flag that indicates about is pull-to-refresh is ongoing.
         * @see Track
         * @see TrackProgress
         * @see StudyPlan
         * @see Topic
         */
        data class Content(
            val track: Track,
            val trackProgress: TrackProgress,
            val studyPlan: StudyPlan? = null,
            val topicsToLearn: List<Topic>,
            val isRefreshing: Boolean = false
        ) : State

        /**
         * Represents a state when track screen data failed to load.
         */
        object NetworkError : State
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean = false) : Message

        data class TrackSuccess(
            val track: Track,
            val trackProgress: TrackProgress,
            val studyPlan: StudyPlan? = null,
            val topicsToLearn: List<Topic>
        ) : Message

        object TrackFailure : Message

        object PullToRefresh : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
        object ClickedContinueInWebEventMessage : Message
    }

    sealed interface Action {
        object FetchTrack : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed interface ViewAction : Action
    }
}