package org.hyperskill.app.progress_screen.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.track.domain.model.TrackWithProgress

object ProgressScreenFeature {
    internal data class State(
        val trackProgressState: TrackProgressState,
        val projectProgressState: ProjectProgressState,
        val isTrackProgressRefreshing: Boolean,
        val isProjectProgressRefreshing: Boolean
    )

    internal sealed interface TrackProgressState {
        object Idle : TrackProgressState
        object Loading : TrackProgressState
        object Error : TrackProgressState
        data class Content(
            val trackWithProgress: TrackWithProgress,
            val studyPlan: StudyPlan,
            val profile: Profile
        ) : TrackProgressState
    }

    internal sealed interface ProjectProgressState {
        object Idle : ProjectProgressState
        object Loading : ProjectProgressState
        object Error : ProjectProgressState
        object Empty : ProjectProgressState
        data class Content(
            val projectWithProgress: ProjectWithProgress,
            val studyPlan: StudyPlan
        ) : ProjectProgressState
    }

    sealed interface Message {
        object Initialize : Message

        object RetryContentLoading : Message
        object RetryTrackProgressLoading : Message
        object RetryProjectProgressLoading : Message

        object PullToRefresh : Message

        object ViewedEventMessage : Message
    }

    internal sealed interface TrackWithProgressFetchResult : Message {
        object Error : TrackWithProgressFetchResult
        data class Success(
            val trackWithProgress: TrackWithProgress,
            val studyPlan: StudyPlan,
            val profile: Profile
        ) : TrackWithProgressFetchResult
    }

    internal sealed interface ProjectWithProgressFetchResult : Message {
        object Error : ProjectWithProgressFetchResult
        object Empty : ProjectWithProgressFetchResult
        data class Success(
            val projectWithProgress: ProjectWithProgress,
            val studyPlan: StudyPlan
        ) : ProjectWithProgressFetchResult
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data class FetchTrackWithProgress(val forceLoadFromNetwork: Boolean) : InternalAction
        data class FetchProjectWithProgress(val forceLoadFromNetwork: Boolean) : InternalAction
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}