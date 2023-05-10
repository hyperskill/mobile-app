package org.hyperskill.app.projects.presentation

import org.hyperskill.app.projects.domain.model.ProjectLevel
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
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
            val projects: Map<Long, ProjectWithProgress>,
            val selectedProjectId: Long?,
            val isRefreshing: Boolean = false,
            val isProjectSelectionLoadingShowed: Boolean = false
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
        val title: String,
        val averageRating: Double,
        val level: ProjectLevel?,
        val timeToComplete: String,
        val isGraduated: Boolean,
        val isBestRated: Boolean,
        val isIdeRequired: Boolean
    )

    sealed interface Message {
        object Initialize : Message
        sealed interface ContentFetchResult : Message {
            data class Success(
                val track: Track,
                val projects: List<ProjectWithProgress>,
                val selectedProjectId: Long?
            ) : ContentFetchResult
            object Error : ContentFetchResult
        }
        object PullToRefresh : Message
        object RetryContentLoading : Message

        data class ProjectClicked(val projectId: Long) : Message

        sealed interface ProjectSelectionResult : Message {
            object Success : ProjectSelectionResult
            object Error : ProjectSelectionResult
        }
    }

    sealed interface Action {
        data class FetchContent(
            val trackId: Long,
            val forceLoadFromNetwork: Boolean
        ) : Action

        data class SelectProject(val trackId: Long, val projectId: Long) : Action

        sealed interface ViewAction : Action {

            sealed interface NavigateTo : ViewAction {
                object HomeScreen : NavigateTo
            }
            sealed interface ShowProjectSelectionStatus : ViewAction {
                object Success : ShowProjectSelectionStatus
                object Error : ShowProjectSelectionStatus
            }
        }
    }
}