package org.hyperskill.app.progresses.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.track.domain.model.TrackWithProgress

object ProgressScreenFeature {
    internal data class State(
        val trackProgressState: TrackProgressState,
        val projectProgressState: ProjectProgressState
    )

    internal sealed interface TrackProgressState {
        object Idle : TrackProgressState
        object Loading : TrackProgressState
        object Error : TrackProgressState
        data class Content(val trackWithProgress: TrackWithProgress) : TrackProgressState
    }

    internal sealed interface ProjectProgressState {
        object Idle : ProjectProgressState
        object Loading : ProjectProgressState
        object Error : ProjectProgressState
        data class Content(val projectWithProgress: ProjectWithProgress) : ProjectProgressState
    }

    sealed interface Message {
        object Initialize : Message
        object RetryContentLoading : Message
        object PullToRefresh : Message
        object BackButtonClicked : Message

        object ViewedEventMessage : Message
    }

    internal sealed interface TrackWithProgressFetchResult : Message {
        object Error : TrackWithProgressFetchResult
        data class Success(val trackWithProgress: TrackWithProgress) : TrackWithProgressFetchResult
    }

    internal sealed interface ProjectWithProgressFetchResult : Message {
        object Error : ProjectWithProgressFetchResult
        data class Success(val projectWithProgress: ProjectWithProgress) : ProjectWithProgressFetchResult
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                object Back : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class FetchTrackWithProgress(val forceLoadFromNetwork: Boolean) : InternalAction
        data class FetchProjectWithProgress(val forceLoadFromNetwork: Boolean) : InternalAction
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}