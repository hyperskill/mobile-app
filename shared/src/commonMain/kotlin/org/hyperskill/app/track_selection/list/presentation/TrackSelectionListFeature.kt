package org.hyperskill.app.track_selection.list.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.track.domain.model.TrackWithProgress

object TrackSelectionListFeature {
    internal sealed interface State {
        data object Idle : State
        data object Loading : State
        data class Content(
            val tracks: List<TrackWithProgress>,
            val selectedTrackId: Long?,
            val tracksSelectionCountMap: Map<Long, Int>
        ) : State
        data object NetworkError : State
    }

    sealed interface ViewState {
        data object Idle : ViewState
        data object Loading : ViewState
        data object Error : ViewState
        data class Content(val tracks: List<Track>) : ViewState

        data class Track(
            val id: Long,
            val imageSource: String?,
            val title: String,
            val timeToComplete: String?,
            val rating: String,
            val isBeta: Boolean,
            val isCompleted: Boolean,
            val isSelected: Boolean,
            val progress: Int
        )
    }

    sealed interface Message {
        data object Initialize : Message

        data object RetryContentLoading : Message

        data class TrackClicked(val trackId: Long) : Message

        /**
         * Analytic
         */
        data object ViewedEventMessage : Message
    }

    /**
     * Internal messages
     */
    internal sealed interface TracksFetchResult : Message {
        data class Success(
            val tracks: List<TrackWithProgress>,
            val selectedTrackId: Long?,
            val tracksSelectionCountMap: Map<Long, Int>
        ) : TracksFetchResult
        data object Error : TracksFetchResult
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                data class TrackDetails(
                    val trackWithProgress: TrackWithProgress,
                    val isTrackSelected: Boolean,
                    val isNewUserMode: Boolean
                ) : NavigateTo
            }

            data object ShowTrackSelectionError : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data object FetchTracks : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}