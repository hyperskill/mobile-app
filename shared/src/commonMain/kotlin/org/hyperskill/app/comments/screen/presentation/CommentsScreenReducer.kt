package org.hyperskill.app.comments.screen.presentation

import org.hyperskill.app.comments.screen.domain.analytic.CommentsScreenClickedRetryContentLoadingHyperskillAnalyticEvent
import org.hyperskill.app.comments.screen.domain.analytic.CommentsScreenClickedShowDiscussionRepliesHyperskillAnalyticEvent
import org.hyperskill.app.comments.screen.domain.analytic.CommentsScreenClickedShowMoreDiscussionsHyperskillAnalyticEvent
import org.hyperskill.app.comments.screen.domain.analytic.CommentsScreenViewedHyperskillAnalyticEvent
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Action
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.DiscussionsState
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.InternalAction
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.InternalMessage
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Message
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.State
import org.hyperskill.app.discussions.domain.model.getRepliesIds
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
            Message.ShowMoreDiscussionsClicked -> handleShowMoreDiscussionsClicked(state)

            is Message.ShowDiscussionRepliesClicked -> handleShowDiscussionRepliesClicked(state, message)
            is InternalMessage.DiscussionRepliesFetchError -> handleDiscussionRepliesFetchError(state, message)
            is InternalMessage.DiscussionRepliesFetchSuccess -> handleDiscussionRepliesFetchSuccess(state, message)

            Message.ViewedEventMessage -> handleViewedEvent(state)
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
                        loadingDiscussionReplies = emptySet(),
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
                        loadingDiscussionReplies = state.discussionsState.loadingDiscussionReplies,
                        isLoadingNextPage = false
                    )
                ) to emptySet()
            }
            else -> null
        }

    private fun handleShowMoreDiscussionsClicked(state: State): CommentsScreenReducerResult? =
        if (state.discussionsState is DiscussionsState.Content &&
            !state.discussionsState.isLoadingNextPage &&
            state.discussionsState.discussions.hasNext
        ) {
            state.updateDiscussionsState(
                state.discussionsState.copy(isLoadingNextPage = true)
            ) to setOf(
                InternalAction.FetchDiscussions(
                    stepId = state.stepRoute.stepId,
                    page = state.discussionsState.discussions.page + 1
                ),
                InternalAction.LogAnalyticEvent(
                    CommentsScreenClickedShowMoreDiscussionsHyperskillAnalyticEvent(state.stepRoute)
                )
            )
        } else {
            null
        }

    private fun handleShowDiscussionRepliesClicked(
        state: State,
        message: Message.ShowDiscussionRepliesClicked
    ): CommentsScreenReducerResult? {
        if (state.discussionsState is DiscussionsState.Content &&
            !state.discussionsState.loadingDiscussionReplies.contains(message.discussionId)
        ) {
            val discussion = state.discussionsState.discussions
                .firstOrNull { it.id == message.discussionId } ?: return null

            return state.updateDiscussionsState(
                state.discussionsState.copy(
                    loadingDiscussionReplies = state.discussionsState.loadingDiscussionReplies + discussion.id
                )
            ) to setOf(
                InternalAction.FetchDiscussionReplies(
                    discussionId = discussion.id,
                    repliesIds = discussion.getRepliesIds().toList()
                ),
                InternalAction.LogAnalyticEvent(
                    CommentsScreenClickedShowDiscussionRepliesHyperskillAnalyticEvent(state.stepRoute)
                )
            )
        } else {
            return null
        }
    }

    private fun handleDiscussionRepliesFetchError(
        state: State,
        message: InternalMessage.DiscussionRepliesFetchError
    ): CommentsScreenReducerResult? =
        if (state.discussionsState is DiscussionsState.Content) {
            state.updateDiscussionsState(
                state.discussionsState.copy(
                    loadingDiscussionReplies = state.discussionsState.loadingDiscussionReplies - message.discussionId
                )
            ) to emptySet()
        } else {
            null
        }

    private fun handleDiscussionRepliesFetchSuccess(
        state: State,
        message: InternalMessage.DiscussionRepliesFetchSuccess
    ): CommentsScreenReducerResult? =
        if (state.discussionsState is DiscussionsState.Content) {
            state.updateDiscussionsState(
                state.discussionsState.copy(
                    commentsMap = state.discussionsState.commentsMap + message.replies.associateBy { it.id },
                    loadingDiscussionReplies = state.discussionsState.loadingDiscussionReplies - message.discussionId
                )
            ) to emptySet()
        } else {
            null
        }

    private fun handleViewedEvent(state: State): CommentsScreenReducerResult =
        state to setOf(InternalAction.LogAnalyticEvent(CommentsScreenViewedHyperskillAnalyticEvent(state.stepRoute)))

    private fun State.updateDiscussionsState(newDiscussionsState: DiscussionsState): State =
        copy(discussionsState = newDiscussionsState)
}