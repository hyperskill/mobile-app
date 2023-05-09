package org.hyperskill.app.track_list.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackWithProgress

interface TrackListFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        data class Content(
            val tracksWithProgresses: List<TrackWithProgress>,
            val selectedTrackId: Long?
        ) : State
        object NetworkError : State
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean) : Message
        sealed interface TracksLoaded : Message {
            data class Success(
                val tracks: List<TrackWithProgress>,
                val selectedTrackId: Long?
            ) : TracksLoaded
            object Error : TracksLoaded
        }

        data class TrackClicked(val trackId: Long) : Message

        data class StartLearningButtonClicked(val trackId: Long) : Message
        sealed interface TrackSelected : Message {
            object Success : TrackSelected
            object Error : TrackSelected
        }

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
        data class TrackModalShownEventMessage(val trackId: Long) : Message
        data class TrackModalHiddenEventMessage(val trackId: Long) : Message
    }

    sealed interface Action {
        object Initialize : Action

        data class SelectTrack(val track: Track) : Action

        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                object HomeScreen : NavigateTo
            }

            data class ShowTrackModal(val track: Track) : ViewAction

            sealed interface ShowTrackSelectionStatus : ViewAction {
                object Loading : ShowTrackSelectionStatus
                object Error : ShowTrackSelectionStatus
                object Success : ShowTrackSelectionStatus
            }
        }

        /**
         * Analytic
         */
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action
    }
}
