package org.hyperskill.app.track.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.track.domain.model.StudyPlan
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackProgress

interface TrackFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        data class Content(
            val track: Track,
            val trackProgress: TrackProgress,
            val studyPlan: StudyPlan? = null
        ) : State
        object NetworkError : State
    }

    sealed interface Message {
        data class Init(val forceUpdate: Boolean = false) : Message

        data class TrackSuccess(
            val track: Track,
            val trackProgress: TrackProgress,
            val studyPlan: StudyPlan? = null
        ) : Message
        data class TrackError(val message: String) : Message

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