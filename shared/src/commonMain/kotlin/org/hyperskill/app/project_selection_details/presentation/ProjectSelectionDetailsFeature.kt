package org.hyperskill.app.project_selection_details.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.providers.domain.model.Provider
import org.hyperskill.app.track.domain.model.Track

object ProjectSelectionDetailsFeature {
    internal data class State(
        val trackId: Long,
        val projectId: Long,
        val isProjectSelected: Boolean,
        val isProjectLoadingShowed: Boolean,
        val contentState: ContentState
    )

    internal sealed interface ContentState {
        object Idle : ContentState
        object Loading : ContentState
        object Error : ContentState
        data class Content(val data: ContentData) : ContentState
    }

    internal class ContentData(
        val track: Track,
        val project: ProjectWithProgress,
        val provider: Provider?
    )

    internal fun initialState(
        trackId: Long,
        projectId: Long,
        isProjectSelected: Boolean
    ): State =
        State(
            trackId = trackId,
            projectId = projectId,
            isProjectSelected = isProjectSelected,
            isProjectLoadingShowed = false,
            contentState = ContentState.Idle
        )

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        object Error : ViewState
        data class Content(
            val formattedTitle: String,
            // Learning outcomes
            val isSelected: Boolean,
            val isIdeRequired: Boolean,
            val learningOutcomesDescription: String?,
            // Project overview
            val formattedAverageRating: String,
            val formattedLevel: String?,
            val formattedGraduateDescription: String?,
            val formattedTimeToComplete: String?,
            // Provided by
            val providerName: String?,
            // CTA
            val isSelectProjectButtonEnabled: Boolean,
            val isProjectLoadingShowed: Boolean
        ) : ViewState
    }

    sealed interface Message {
        object Initialize : Message
        object RetryContentLoading : Message

        object SelectProjectButtonClicked : Message

        object ViewedEventMessage : Message
    }

    internal sealed interface ContentFetchResult : Message {
        object Error : ContentFetchResult
        data class Success(val data: ContentData) : ContentFetchResult
    }

    internal sealed interface ProjectSelectionResult : Message {
        object Error : ProjectSelectionResult
        object Success : ProjectSelectionResult
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface ShowProjectSelectionStatus : ViewAction {
                object Error : ShowProjectSelectionStatus
                object Success : ShowProjectSelectionStatus
            }

            sealed interface NavigateTo : ViewAction {
                object StudyPlan : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class FetchContent(
            val trackId: Long,
            val projectId: Long,
            val forceLoadFromNetwork: Boolean
        ) : InternalAction

        data class SelectProject(
            val trackId: Long,
            val projectId: Long
        ) : InternalAction

        /**
         * Clears projects cache, because don't need it anymore.
         *
         * @see [Action.ViewAction.NavigateTo.StudyPlan]
         */
        object ClearProjectsCache : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}