package org.hyperskill.app.comments.screen.presentation

import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.CommentStatisticsEntry
import org.hyperskill.app.comments.screen.domain.model.CommentsScreenFeatureParams
import org.hyperskill.app.discussions.domain.model.Discussion
import org.hyperskill.app.discussions.remote.model.DiscussionsResponse
import ru.nobird.app.core.model.PagedList

object CommentsScreenFeature {
    internal data class State(
        val stepId: Long,
        val commentStatistics: CommentStatisticsEntry,
        val discussionsState: DiscussionsState,
        val commentsState: CommentsState
    )

    internal fun initialState(params: CommentsScreenFeatureParams) =
        State(
            stepId = params.stepId,
            commentStatistics = params.commentStatistics,
            discussionsState = DiscussionsState.Idle,
            commentsState = CommentsState()
        )

    internal sealed interface DiscussionsState {
        object Idle : DiscussionsState
        object Loading : DiscussionsState
        object Error : DiscussionsState
        data class Content(
            val discussions: PagedList<Discussion>,
            val isLoadingNextPage: Boolean
        ) : DiscussionsState
    }

    internal data class CommentsState(
        val commentsMap: Map<Long, Comment> = emptyMap()
    )

    data class ViewState(
        val navigationTitle: String
    )

    sealed interface Message {
        object Initialize : Message
        object RetryContentLoading : Message
    }

    internal sealed interface InternalMessage : Message {
        object DiscussionsFetchError : InternalMessage
        data class DiscussionsFetchSuccess(
            val discussionsResponse: DiscussionsResponse,
            val rootComments: List<Comment>
        ) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data class FetchDiscussions(val stepId: Long, val page: Int) : InternalAction
    }
}