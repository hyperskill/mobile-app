package org.hyperskill.app.projects.presentation

import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.track.domain.model.Track

object ProjectsListFeature {
    data class State(
        val trackId: Long,
        val content: ContentState
    )

    sealed interface ContentState {
        object Idle : ContentState
        object Loading : ContentState
        data class Content(
            val track: Track,
            val projects: Map<Long, Project>,
            val selectedProjectId: Long?,
            val isRefreshing: Boolean = false
        ) : ContentState
        object Error : ContentState
    }

    fun initialState(trackId: Long): State =
        State(trackId, ContentState.Idle)

    sealed interface Message {
        object Initialize : Message
        sealed interface ContentFetchResult : Message {
            data class Success(
                val track: Track,
                val projects: List<Project>,
                val selectedProjectId: Long?
            ) : ContentFetchResult
            object Error : ContentFetchResult
        }
        object PullToRefresh : Message
        object RetryContentLoading : Message
    }

    sealed interface Action {
        data class FetchContent(
            val trackId: Long,
            val forceLoadFromNetwork: Boolean
        ) : Action
    }
}