package org.hyperskill.app.projects.presentation

import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.domain.model.ProjectLevel
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

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        data class Content(
            val formattedTitle: String,
            val selectedProject: ProjectListItem?,
            val recommendedProjects: List<ProjectListItem>,
            val projectsByLevel: Map<ProjectLevel, List<ProjectListItem>>,
            val isRefreshing: Boolean = false
        ) : ViewState
        object Error : ViewState
    }

    data class ProjectListItem(
        val id: Long,
        val title: String
    )

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