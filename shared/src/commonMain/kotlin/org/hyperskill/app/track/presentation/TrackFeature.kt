package org.hyperskill.app.track.presentation

import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackProgress

interface TrackFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        data class Content(val track: Track, val trackProgress: TrackProgress) : State
        object NetworkError : State
    }

    sealed interface Message {
        data class Init(
            val trackId: Long,
            val forceUpdate: Boolean
        ) : Message

        data class TrackSuccess(val track: Track, val trackProgress: TrackProgress) : Message
        data class TrackError(val message: String) : Message
    }

    sealed interface Action {
        data class FetchTrack(val trackId: Long) : Action
    }
}