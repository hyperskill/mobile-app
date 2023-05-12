package org.hyperskill.app.project_selection.presentation

import org.hyperskill.app.project_selection.domain.analytic.ProjectsSelectionListClickedProjectHyperskillAnalyticsEvent
import org.hyperskill.app.project_selection.domain.analytic.ProjectsSelectionListClickedRetryContentLoadingHyperskillAnalyticsEvent
import org.hyperskill.app.project_selection.domain.analytic.ProjectsSelectionListViewedHyperskillAnalyticEvent
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.Action
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.Action.ViewAction
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.ContentState
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.InternalAction
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.Message
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ProjectsListReducerResult = Pair<State, Set<Action>>

internal class ProjectSelectionListReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ProjectsListReducerResult =
        when (message) {
            Message.Initialize -> {
                state.updateContentState(ContentState.Loading) to
                    fetchContent(state)
            }
            is ProjectSelectionListFeature.ContentFetchResult.Success -> {
                state.updateContentState(
                    ContentState.Content(
                        track = message.track,
                        projects = message.projects.associateBy { it.project.id },
                        selectedProjectId = message.selectedProjectId
                    )
                ) to emptySet()
            }
            ProjectSelectionListFeature.ContentFetchResult.Error -> {
                state.updateContentState(ContentState.Error) to emptySet()
            }
            Message.RetryContentLoading -> {
                if (state.content is ContentState.Error) {
                    state.updateContentState(ContentState.Loading) to
                        fetchContent(state, forceLoadFromNetwork = true) +
                            InternalAction.LogAnalyticEvent(
                                ProjectsSelectionListClickedRetryContentLoadingHyperskillAnalyticsEvent(
                                    trackId = state.trackId
                                )
                            )
                } else {
                    state to emptySet()
                }
            }
            is Message.ProjectClicked -> {
                if (state.content is ContentState.Content) {
                    state.copy(
                        content = state.content.copy(isProjectSelectionLoadingShowed = true)
                    ) to setOf(
                        InternalAction.SelectProject(
                            trackId = state.trackId,
                            projectId = message.projectId
                        ),
                        InternalAction.LogAnalyticEvent(
                            ProjectsSelectionListClickedProjectHyperskillAnalyticsEvent(
                                trackId = state.trackId,
                                projectId = message.projectId
                            )
                        )
                    )
                } else {
                    state to emptySet()
                }
            }
            is ProjectSelectionListFeature.ProjectSelectionResult -> {
                if (state.content is ContentState.Content) {
                    state.copy(
                        content = state.content.copy(isProjectSelectionLoadingShowed = false)
                    ) to when (message) {
                        ProjectSelectionListFeature.ProjectSelectionResult.Error ->
                            setOf(ViewAction.ShowProjectSelectionStatus.Error)
                        ProjectSelectionListFeature.ProjectSelectionResult.Success ->
                            setOf(
                                ViewAction.NavigateTo.StudyPlan,
                                ViewAction.ShowProjectSelectionStatus.Success
                            )
                    }
                } else {
                    state to emptySet()
                }
            }
            Message.ViewedEventMessage -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        ProjectsSelectionListViewedHyperskillAnalyticEvent(state.trackId)
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

    private fun State.updateContentState(contentState: ContentState): State =
        copy(content = contentState)
}