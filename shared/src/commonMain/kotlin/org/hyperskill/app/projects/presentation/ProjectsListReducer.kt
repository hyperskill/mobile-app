package org.hyperskill.app.projects.presentation

import org.hyperskill.app.projects.domain.analytic.ProjectsListClickedProjectHyperskillAnalyticsEvent
import org.hyperskill.app.projects.domain.analytic.ProjectsListClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.projects.domain.analytic.ProjectsListClickedRetryContentLoadingHyperskillAnalyticsEvent
import org.hyperskill.app.projects.domain.analytic.ProjectsListViewedHyperskillAnalyticEvent
import org.hyperskill.app.projects.presentation.ProjectsListFeature.Action
import org.hyperskill.app.projects.presentation.ProjectsListFeature.Action.ViewAction
import org.hyperskill.app.projects.presentation.ProjectsListFeature.ContentState
import org.hyperskill.app.projects.presentation.ProjectsListFeature.InternalAction
import org.hyperskill.app.projects.presentation.ProjectsListFeature.InternalMessage
import org.hyperskill.app.projects.presentation.ProjectsListFeature.Message
import org.hyperskill.app.projects.presentation.ProjectsListFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ProjectsListReducerResult = Pair<State, Set<Action>>

internal class ProjectsListReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ProjectsListReducerResult =
        when (message) {
            Message.Initialize -> {
                state.updateContentState(ContentState.Loading) to
                    fetchContent(state)
            }
            is InternalMessage.ContentFetchResult.Success -> {
                state.updateContentState(
                    ContentState.Content(
                        track = message.track,
                        projects = message.projects.associateBy { it.project.id },
                        selectedProjectId = message.selectedProjectId,
                        isRefreshing = false
                    )
                ) to emptySet()
            }
            InternalMessage.ContentFetchResult.Error -> {
                state.updateContentState(ContentState.Error) to emptySet()
            }
            Message.PullToRefresh -> {
                state.doIfContent { content ->
                    if (content.isRefreshing) {
                        content to emptySet()
                    } else {
                        content.copy(isRefreshing = true) to
                            fetchContent(state, forceLoadFromNetwork = true) +
                                InternalAction.LogAnalyticEvent(
                                    ProjectsListClickedPullToRefreshHyperskillAnalyticEvent(
                                        trackId = state.trackId
                                    )
                                )
                    }
                }
            }
            Message.RetryContentLoading -> {
                state.doIf<ContentState.Error> {
                    ContentState.Loading to
                        fetchContent(state, forceLoadFromNetwork = true) +
                            InternalAction.LogAnalyticEvent(
                                ProjectsListClickedRetryContentLoadingHyperskillAnalyticsEvent(
                                    trackId = state.trackId
                                )
                            )
                }
            }
            is Message.ProjectClicked -> {
                state.doIfContent { content ->
                    content.copy(isProjectSelectionLoadingShowed = true) to
                        setOf(
                            InternalAction.SelectProject(
                                trackId = state.trackId,
                                projectId = message.projectId
                            ),
                            InternalAction.LogAnalyticEvent(
                                ProjectsListClickedProjectHyperskillAnalyticsEvent(
                                    trackId = state.trackId,
                                    projectId = message.projectId
                                )
                            )
                        )
                }
            }
            is InternalMessage.ProjectSelectionResult -> {
                state.doIfContent { content ->
                    content.copy(isProjectSelectionLoadingShowed = false) to
                        when (message) {
                            InternalMessage.ProjectSelectionResult.Error ->
                                setOf(ViewAction.ShowProjectSelectionStatus.Error)
                            InternalMessage.ProjectSelectionResult.Success ->
                                setOf(
                                    ViewAction.NavigateTo.HomeScreen,
                                    ViewAction.ShowProjectSelectionStatus.Success
                                )
                        }
                }
            }
            Message.ViewedEventMessage -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        ProjectsListViewedHyperskillAnalyticEvent(state.trackId)
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

    private inline fun <reified T : ContentState> State.doIf(
        block: (T) -> Pair<ContentState, Set<Action>>
    ): ProjectsListReducerResult =
        if (content is T) {
            val (contentState, actions) = block(content)
            this.copy(content = contentState) to actions
        } else {
            this to emptySet()
        }

    private inline fun State.doIfContent(
        block: (ContentState.Content) -> Pair<ContentState, Set<Action>>
    ): ProjectsListReducerResult =
        doIf(block)
}