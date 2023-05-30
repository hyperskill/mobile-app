package org.hyperskill.app.track_selection.details.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.providers.domain.model.Provider
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType
import org.hyperskill.app.track.domain.model.TrackWithProgress

object TrackSelectionDetailsFeature {
    internal data class State(
        val trackWithProgress: TrackWithProgress,
        val isTrackSelected: Boolean,
        val isNewUserMode: Boolean,
        val isTrackLoadingShowed: Boolean,
        val contentState: ContentState
    )

    internal sealed interface ContentState {
        object Idle : ContentState
        object Loading : ContentState
        data class Content(
            val subscriptionType: SubscriptionType,
            val profile: Profile,
            val providers: List<Provider>
        ) : ContentState {
            val isFreemiumEnabled: Boolean
                get() = subscriptionType == SubscriptionType.FREEMIUM
        }
        object NetworkError : ContentState
    }

    internal fun initialState(
        trackWithProgress: TrackWithProgress,
        isTrackSelected: Boolean,
        isNewUserMode: Boolean
    ) =
        State(
            trackWithProgress = trackWithProgress,
            isTrackSelected = isTrackSelected,
            isTrackLoadingShowed = false,
            isNewUserMode = isNewUserMode,
            contentState = ContentState.Idle
        )

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        data class Content(
            val title: String,
            val description: String?,
            val formattedRating: String,
            val formattedTimeToComplete: String?,
            val formattedTopicsCount: String,
            val formattedProjectsCount: String?,
            val isCertificateAvailable: Boolean,
            val mainProvider: MainProvider?,
            val formattedOtherProviders: String?,
            val isBeta: Boolean,
            val isCompleted: Boolean,
            val isSelected: Boolean,
            val isSelectTrackButtonEnabled: Boolean,
            val isTrackSelectionLoadingShowed: Boolean
        ) : ViewState {
            val areTagsVisible: Boolean
                get() = isBeta || isCompleted || isSelected
        }

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

    internal sealed interface FetchAdditionalInfoResult : Message {
        data class Success(
            val subscriptionType: SubscriptionType,
            val profile: Profile,
            val providers: List<Provider>
        ) : FetchAdditionalInfoResult
        object Error : FetchAdditionalInfoResult
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

            sealed interface NavigateTo : ViewAction {
                object StudyPlan : NavigateTo
                object Home : NavigateTo
                data class ProjectSelectionList(val trackId: Long) : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class FetchAdditionalInfo(
            val providerIds: List<Long>,
            val forceLoadFromNetwork: Boolean
        ) : InternalAction

        data class SelectTrack(val trackId: Long) : InternalAction

        data class LogAnalyticEvent(val event: HyperskillAnalyticEvent) : InternalAction
    }
}