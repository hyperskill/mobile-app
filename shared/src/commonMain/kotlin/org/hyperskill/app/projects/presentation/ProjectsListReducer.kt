package org.hyperskill.app.projects.presentation

import org.hyperskill.app.projects.presentation.ProjectsListFeature.Action
import org.hyperskill.app.projects.presentation.ProjectsListFeature.ContentState
import org.hyperskill.app.projects.presentation.ProjectsListFeature.Message
import org.hyperskill.app.projects.presentation.ProjectsListFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class ProjectsListReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            Message.Initialize -> {
                state.updateContentState(ContentState.Loading) to
                    setOf(Action.FetchContent(state.trackId, forceLoadFromNetwork = false))
            }
            is Message.ContentFetchResult.Success -> {
                state.updateContentState(
                    ContentState.Content(
                        track = message.track,
                        projects = message.projects.associateBy { it.id },
                        selectedProjectId = message.selectedProjectId,
                        isRefreshing = false
                    )
                ) to emptySet()
            }
            Message.ContentFetchResult.Error -> {
                state.updateContentState(ContentState.Error) to emptySet()
            }
            Message.PullToRefresh -> {
                state.updateContentStateIfContent { content ->
                    if (content.isRefreshing) {
                        content
                    } else {
                        content.copy(isRefreshing = true)
                    }
                } to setOf(Action.FetchContent(state.trackId, forceLoadFromNetwork = true))
            }
            Message.RetryContentLoading -> {
                state.updateContentState(ContentState.Loading) to
                    setOf(Action.FetchContent(state.trackId, forceLoadFromNetwork = true))
            }
        }

    private fun State.updateContentState(contentState: ContentState): State =
        copy(content = contentState)

    private fun State.updateContentStateIfContent(block: (ContentState.Content) -> ContentState.Content): State =
        if (content is ContentState.Content) {
            copy(content = block(content))
        } else {
            this
        }
}