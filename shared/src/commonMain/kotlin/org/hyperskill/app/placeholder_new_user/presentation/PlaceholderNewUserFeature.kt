package org.hyperskill.app.placeholder_new_user.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.track.domain.model.Track

interface PlaceholderNewUserFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        data class Content(val tracks: List<Track>) : State
        object NetworkError : State
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean) : Message

        sealed interface TracksLoaded : Message {
            data class Success(val tracks: List<Track>) : TracksLoaded
            object Error : TracksLoaded
        }

        data class StartLearningButtonClicked(val trackId: Long) : Message
        sealed interface TrackSelected : Message {
            object Success : TrackSelected
            object Error : TrackSelected
        }

        data class TrackClicked(val trackId: Long) : Message

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
