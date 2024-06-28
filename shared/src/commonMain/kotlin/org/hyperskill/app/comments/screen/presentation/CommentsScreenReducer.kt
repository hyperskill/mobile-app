package org.hyperskill.app.comments.screen.presentation

import org.hyperskill.app.comments.screen.domain.analytic.CommentsScreenClickedRetryContentLoadingHyperskillAnalyticEvent
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Action
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.DiscussionsState
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.InternalAction
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.InternalMessage
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Message
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.State
import org.hyperskill.app.discussions.remote.model.toPagedList
import ru.nobird.app.core.model.plus
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias CommentsScreenReducerResult = Pair<State, Set<Action>>

internal class CommentsScreenReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): CommentsScreenReducerResult =
        when (message) {
            Message.Initialize -> handleInitialize(state)
            Message.RetryContentLoading -> handleRetryContentLoading(state)
            InternalMessage.DiscussionsFetchError -> handleDiscussionsFetchError(state)
            is InternalMessage.DiscussionsFetchSuccess -> handleDiscussionsFetchSuccess(state, message)
        } ?: (state to emptySet())

    private fun handleInitialize(state: State): CommentsScreenReducerResult? =
        if (state.discussionsState is DiscussionsState.Idle) {
            state.updateDiscussionsState(DiscussionsState.Loading) to
                setOf(InternalAction.FetchDiscussions(stepId = state.stepRoute.stepId, page = 1))
        } else {
            null
        }

    private fun handleRetryContentLoading(state: State): CommentsScreenReducerResult? =
        if (state.discussionsState is DiscussionsState.Error) {
            state.updateDiscussionsState(DiscussionsState.Loading) to
                setOf(
                    InternalAction.FetchDiscussions(stepId = state.stepRoute.stepId, page = 1),
                    InternalAction.LogAnalyticEvent(
                        CommentsScreenClickedRetryContentLoadingHyperskillAnalyticEvent(state.stepRoute)
                    )
                )
        } else {
            null
        }

    private fun handleDiscussionsFetchError(state: State): CommentsScreenReducerResult? =
        if (state.discussionsState is DiscussionsState.Loading) {
            state.updateDiscussionsState(DiscussionsState.Error) to emptySet()
        } else {
            null
        }

    private fun handleDiscussionsFetchSuccess(
        state: State,
        message: InternalMessage.DiscussionsFetchSuccess
    ): CommentsScreenReducerResult? =
        when (state.discussionsState) {
            DiscussionsState.Loading ->
                state.updateDiscussionsState(
                    DiscussionsState.Content(
                        discussions = message.discussionsResponse.toPagedList(),
                        commentsMap = message.rootComments.associateBy { it.id },
                        isLoadingNextPage = false
                    )
                ) to emptySet()
            is DiscussionsState.Content -> {
                val newDiscussions =
                    state.discussionsState.discussions.plus(message.discussionsResponse.toPagedList())
                val newCommentsMap =
                    state.discussionsState.commentsMap + message.rootComments.associateBy { it.id }

                state.updateDiscussionsState(
                    DiscussionsState.Content(
                        discussions = newDiscussions,
                        commentsMap = newCommentsMap,
                        isLoadingNextPage = false
                    )
                ) to emptySet()
            }
            else -> null
        }

    private fun State.updateDiscussionsState(newDiscussionsState: DiscussionsState): State =
        copy(discussionsState = newDiscussionsState)
}