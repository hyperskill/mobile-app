package org.hyperskill.app.progresses.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.projects.domain.model.ProjectLevel
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

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        object Error : ViewState
        data class Content(
            val trackProgress: TrackProgressViewState,
            val projectProgress: ProjectProgressViewState?
        ) : ViewState

        data class TrackProgressViewState(
            val title: String,

            val imageSource: String?,

            val completedTopicsCountLabel: String,
            val completedTopicsPercentageLabel: String,
            val completedTopicsPercentageProgress: Float,

            val appliedTopicsCountLabel: String,
            val appliedTopicsPercentageLabel: String,
            val appliedTopicsPercentageProgress: Float,

            val timeToCompleteLabel: String?,

            val completedGraduateProjectsCount: Int,

            val isCompleted: Boolean
        )

        data class ProjectProgressViewState(
            val title: String,

            val level: ProjectLevel?,

            val timeToCompleteLabel: String?,

            val completedStagesLabel: String,
            val completedStagesProgress: Float,

            val isCompleted: Boolean
        )
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