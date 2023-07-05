package org.hyperskill.app.project_selection.list.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.projects.domain.model.ProjectLevel
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.track.domain.model.Track

object ProjectSelectionListFeature {

    internal const val BEST_RATED_PROJECTS_COUNT = 6

    internal data class State(
        val trackId: Long,
        val isNewUserMode: Boolean,
        val content: ContentState
    )

    internal sealed interface ContentState {
        object Idle : ContentState
        object Loading : ContentState
        data class Content(
            val track: Track,
            val projects: Map<Long, ProjectWithProgress>,
            val sortedProjectsIds: List<Long>,
            val currentProjectId: Long?
        ) : ContentState
        object Error : ContentState
    }

    internal fun initialState(
        trackId: Long,
        isNewUserMode: Boolean
    ): State =
        State(trackId, isNewUserMode, ContentState.Idle)

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        data class Content(
            val trackIcon: String?,
            val formattedTitle: String,
            val selectedProject: ProjectListItem?,
            val recommendedProjects: List<ProjectListItem>,
            val projectsByLevel: Map<ProjectLevel, List<ProjectListItem>>
        ) : ViewState
        object Error : ViewState
    }

    /**
     * A view representation of a project.
     */
    data class ProjectListItem(
        val id: Long,
        val title: String,
        val averageRating: String,
        val level: ProjectLevel?,
        val formattedTimeToComplete: String?,
        val isGraduate: Boolean,
        val isBestRated: Boolean,
        val isIdeRequired: Boolean,
        val isFastestToComplete: Boolean,
        val isCompleted: Boolean,
        val isBeta: Boolean,
        val progress: Int
    )

    sealed interface Message {
        object Initialize : Message
        object RetryContentLoading : Message

        data class ProjectClicked(val projectId: Long) : Message

        object ViewedEventMessage : Message
    }

    internal sealed interface ContentFetchResult : Message {
        data class Success(
            val profile: Profile,
            val track: Track,
            val projects: List<ProjectWithProgress>,
            val currentProjectId: Long?
        ) : ContentFetchResult

        object Error : ContentFetchResult
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            object ShowProjectSelectionError : ViewAction

            sealed interface NavigateTo : ViewAction {
                data class ProjectDetails(
                    val isNewUserMode: Boolean,
                    val trackId: Long,
                    val projectId: Long,
                    val isProjectSelected: Boolean,
                    val isProjectBestRated: Boolean,
                    val isProjectFastestToComplete: Boolean
                ) : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class FetchContent(
            val trackId: Long,
            val forceLoadFromNetwork: Boolean
        ) : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}