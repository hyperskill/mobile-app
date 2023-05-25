package org.hyperskill.app.project_selection.list.presentation

import org.hyperskill.app.profile.domain.model.isRecommendationsJavaProjectsFeatureEnabled
import org.hyperskill.app.profile.domain.model.isRecommendationsKotlinProjectsFeatureEnabled
import org.hyperskill.app.profile.domain.model.isRecommendationsPythonProjectsFeatureEnabled
import org.hyperskill.app.project_selection.list.domain.analytic.ProjectSelectionListClickedProjectHyperskillAnalyticsEvent
import org.hyperskill.app.project_selection.list.domain.analytic.ProjectSelectionListClickedRetryContentLoadingHyperskillAnalyticsEvent
import org.hyperskill.app.project_selection.list.domain.analytic.ProjectSelectionListViewedHyperskillAnalyticEvent
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.Action
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.Action.ViewAction
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.ContentState
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.InternalAction
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.Message
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.State
import org.hyperskill.app.projects.domain.model.sortByScore
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ProjectsListReducerResult = Pair<State, Set<Action>>

internal class ProjectSelectionListReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ProjectsListReducerResult =
        when (message) {
            Message.Initialize -> {
                state.updateContentState(ContentState.Loading) to
                    fetchContent(state)
            }
            is ProjectSelectionListFeature.ContentFetchResult.Success ->
                handleContentFetchResultSuccess(state, message)
            ProjectSelectionListFeature.ContentFetchResult.Error -> {
                state.updateContentState(ContentState.Error) to emptySet()
            }
            Message.RetryContentLoading -> {
                if (state.content is ContentState.Error) {
                    state.updateContentState(ContentState.Loading) to
                        fetchContent(state, forceLoadFromNetwork = true) +
                            InternalAction.LogAnalyticEvent(
                                ProjectSelectionListClickedRetryContentLoadingHyperskillAnalyticsEvent(
                                    trackId = state.trackId
                                )
                            )
                } else {
                    state to emptySet()
                }
            }
            is Message.ProjectClicked -> {
                handleProjectClickedMessage(state, message)
            }
            Message.ViewedEventMessage -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        ProjectSelectionListViewedHyperskillAnalyticEvent(state.trackId)
                    )
                )
            }
        }

    private fun fetchContent(
        state: State,
        forceLoadFromNetwork: Boolean = false
    ): Set<Action> =
        setOf(
            InternalAction.FetchContent(
                state.trackId,
                forceLoadFromNetwork = forceLoadFromNetwork
            )
        )

    private fun handleContentFetchResultSuccess(
        state: State,
        message: ProjectSelectionListFeature.ContentFetchResult.Success
    ): ProjectsListReducerResult {
        val useFeatureScoreForSorting =
            with(message.profile.features) {
                isRecommendationsJavaProjectsFeatureEnabled ||
                    isRecommendationsKotlinProjectsFeatureEnabled ||
                    isRecommendationsPythonProjectsFeatureEnabled
            }
        val sortedProjectIds =
            message.projects
                .sortByScore(useFeatureScoreForSorting)
                .map { it.project.id }
        return state.updateContentState(
            ContentState.Content(
                track = message.track,
                projects = message.projects.associateBy { it.project.id },
                sortedProjectsIds = sortedProjectIds,
                currentProjectId = message.currentProjectId
            )
        ) to emptySet()
    }

    private fun handleProjectClickedMessage(
        state: State,
        message: Message.ProjectClicked
    ): ProjectsListReducerResult {
        val analyticEventAction = InternalAction.LogAnalyticEvent(
            ProjectSelectionListClickedProjectHyperskillAnalyticsEvent(
                trackId = state.trackId,
                projectId = message.projectId
            )
        )

        val project = (state.content as? ContentState.Content)?.projects?.get(message.projectId)?.project
        val currentProjectId = (state.content as? ContentState.Content)?.currentProjectId

        return if (project != null) {
            state to setOf(
                analyticEventAction,
                ViewAction.NavigateTo.ProjectDetails(
                    trackId = state.trackId,
                    projectId = project.id,
                    isProjectSelected = project.id == currentProjectId
                )
            )
        } else {
            state to setOf(
                analyticEventAction,
                ViewAction.ShowProjectSelectionError
            )
        }
    }

    private fun State.updateContentState(contentState: ContentState): State =
        copy(content = contentState)
}