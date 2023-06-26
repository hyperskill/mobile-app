package org.hyperskill.app.progresses.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.track.domain.model.TrackWithProgress

object ProgressScreenFeature {
    internal sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(
            val trackWithProgress: TrackWithProgress,
            val projectWithProgress: ProjectWithProgress
        ) : State
    }

    sealed interface Message {
        object Initialize : Message
        object RetryContentLoading : Message
        object PullToRefresh : Message
        object BackButtonClicked : Message

        object ViewedEventMessage : Message
    }

    internal sealed interface ContentFetchResult : Message {
        object Error : ContentFetchResult
        data class Success(
            val trackWithProgress: TrackWithProgress,
            val projectWithProgress: ProjectWithProgress
        ) : ContentFetchResult
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                object Back : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        object FetchContent : InternalAction
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}