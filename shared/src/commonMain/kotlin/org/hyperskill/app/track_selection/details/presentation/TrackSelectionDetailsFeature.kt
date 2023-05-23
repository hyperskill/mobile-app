package org.hyperskill.app.track_selection.details.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.providers.domain.model.Provider
import org.hyperskill.app.track.domain.model.TrackWithProgress

object TrackSelectionDetailsFeature {
    internal data class State(
        val trackWithProgress: TrackWithProgress,
        val isTrackSelected: Boolean,
        val isTrackLoadingShowed: Boolean,
        val contentState: ContentState
    )

    internal sealed interface ContentState {
        object Idle : ContentState
        object Loading : ContentState
        data class Content(
            val isFreemiumEnabled: Boolean,
            val providers: List<Provider>
        ) : ContentState
        object NetworkError : ContentState
    }

    internal fun initialState(
        trackWithProgress: TrackWithProgress,
        isTrackSelected: Boolean
    ) =
        State(
            trackWithProgress = trackWithProgress,
            isTrackSelected = isTrackSelected,
            isTrackLoadingShowed = false,
            contentState = ContentState.Idle
        )

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        data class Content(
            val title: String,
            val description: String,
            val formattedRating: String,
            val formattedTimeToComplete: String?,
            val formattedTopicsCount: String,
            val formattedProjectsCount: String?,
            val isCertificateAvailable: Boolean,
            val mainProvider: MainProvider?,
            val formattedOtherProviders: String?,
            val isBeta: Boolean,
            val isCompleted: Boolean,
            val isSelected: Boolean
        ) : ViewState

        data class MainProvider(
            val title: String,
            val description: String
        )

        object NetworkError : ViewState
    }

    sealed interface Message {
        object Initialize : Message

        object RetryContentLoading : Message

        object SelectTrackButtonClicked : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
    }

    internal sealed interface FetchProvidersAndFreemiumStatusResult : Message {
        data class Success(
            val isFreemiumEnabled: Boolean,
            val providers: List<Provider>
        ) : FetchProvidersAndFreemiumStatusResult
        object Error : FetchProvidersAndFreemiumStatusResult
    }

    internal sealed interface TrackSelectionResult : Message {
        object Success : TrackSelectionResult
        object Error : TrackSelectionResult
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface ShowTrackSelectionStatus : ViewAction {
                object Success : ShowTrackSelectionStatus
                object Error : ShowTrackSelectionStatus
            }
        }
    }

    sealed interface InternalAction : Action {
        data class FetchProvidersAndFreemiumStatus(
            val providerIds: List<Long>,
            val forceLoadFromNetwork: Boolean
        ) : InternalAction

        data class SelectTrack(val trackId: Long) : InternalAction

        data class LogAnalyticEvent(val event: HyperskillAnalyticEvent) : InternalAction
    }
}