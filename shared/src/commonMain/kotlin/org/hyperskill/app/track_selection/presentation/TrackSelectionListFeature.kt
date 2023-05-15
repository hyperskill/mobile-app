package org.hyperskill.app.track_selection.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackWithProgress

object TrackSelectionListFeature {
    internal sealed interface State {
        object Idle : State
        object Loading : State
        data class Content(
            val tracks: List<TrackWithProgress>,
            val selectedTrackId: Long?
        ) : State
        object NetworkError : State
    }

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        object Error : ViewState
        data class Content(
            val selectedTrack: Track?,
            val tracks: List<Track>
        ) : ViewState

        data class Track(
            val id: Long,
            val imageSource: String,
            val title: String,
            val description: String,
            val timeToComplete: String?,
            val rating: String,
            val isBeta: Boolean,
            val isCompleted: Boolean
        )
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean) : Message

        data class TrackClicked(val trackId: Long) : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
    }

    /**
     * Internal messages
     */
    internal sealed interface TracksFetchResult : Message {
        data class Success(
            val tracks: List<TrackWithProgress>,
            val selectedTrackId: Long?
        ) : TracksFetchResult
        object Error : TracksFetchResult
    }

    internal sealed interface TrackSelectionResult : Message {
        object Success : TrackSelectionResult
        object Error : TrackSelectionResult
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                object StudyPlan : NavigateTo
            }

            sealed interface ShowTrackSelectionStatus : ViewAction {
                object Loading : ShowTrackSelectionStatus
                object Error : ShowTrackSelectionStatus
                object Success : ShowTrackSelectionStatus
            }
        }
    }

    internal sealed interface InternalAction : Action {
        object Initialize : InternalAction

        data class SelectTrack(val track: Track) : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}