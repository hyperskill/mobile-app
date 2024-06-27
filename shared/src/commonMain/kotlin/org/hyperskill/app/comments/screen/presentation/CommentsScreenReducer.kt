package org.hyperskill.app.comments.screen.presentation

import org.hyperskill.app.comments.domain.model.Comment
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
                setOf(InternalAction.FetchDiscussions(stepId = state.stepId, page = 1))
        } else {
            null
        }

    private fun handleRetryContentLoading(state: State): CommentsScreenReducerResult? =
        if (state.discussionsState is DiscussionsState.Error) {
            state.updateDiscussionsState(DiscussionsState.Loading) to
                setOf(InternalAction.FetchDiscussions(stepId = state.stepId, page = 1))
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
                state
                    .updateDiscussionsState(
                        DiscussionsState.Content(
                            discussions = message.discussionsResponse.toPagedList(),
                            isLoadingNextPage = false
                        )
                    )
                    .putAllComments(message.rootComments) to emptySet()
            is DiscussionsState.Content -> {
                val newDiscussions =
                    state.discussionsState.discussions.plus(message.discussionsResponse.toPagedList())

                state
                    .updateDiscussionsState(
                        DiscussionsState.Content(
                            discussions = newDiscussions,
                            isLoadingNextPage = false
                        )
                    )
                    .putAllComments(message.rootComments) to emptySet()
            }
            else -> null
        }

    private fun State.updateDiscussionsState(newDiscussionsState: DiscussionsState): State =
        copy(discussionsState = newDiscussionsState)

    private fun State.putAllComments(newComments: List<Comment>): State {
        val newCommentsMap = newComments.associateBy { it.id }
        return copy(commentsState = commentsState.copy(commentsMap = commentsState.commentsMap + newCommentsMap))
    }
}