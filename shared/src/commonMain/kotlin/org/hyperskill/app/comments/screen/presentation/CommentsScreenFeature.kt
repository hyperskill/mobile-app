package org.hyperskill.app.comments.screen.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.CommentStatisticsEntry
import org.hyperskill.app.comments.screen.domain.model.CommentsScreenFeatureParams
import org.hyperskill.app.discussions.domain.model.Discussion
import org.hyperskill.app.discussions.remote.model.DiscussionsResponse
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.core.model.PagedList

object CommentsScreenFeature {
    internal data class State(
        val stepRoute: StepRoute,
        val commentStatistics: CommentStatisticsEntry,
        val discussionsState: DiscussionsState
    )

    internal fun initialState(params: CommentsScreenFeatureParams) =
        State(
            stepRoute = params.stepRoute,
            commentStatistics = params.commentStatistics,
            discussionsState = DiscussionsState.Idle
        )

    internal sealed interface DiscussionsState {
        object Idle : DiscussionsState
        object Loading : DiscussionsState
        object Error : DiscussionsState
        data class Content(
            val discussions: PagedList<Discussion>,
            val commentsMap: Map<Long, Comment>,
            val loadingDiscussionReplies: Set<Long>,
            val isLoadingNextPage: Boolean
        ) : DiscussionsState
    }

    sealed interface Message {
        object Initialize : Message
        object RetryContentLoading : Message

        data class ShowDiscussionRepliesClicked(val discussionId: Long) : Message
        object ShowMoreDiscussionsClicked : Message

        object ViewedEventMessage : Message
    }

    internal sealed interface InternalMessage : Message {
        object DiscussionsFetchError : InternalMessage
        data class DiscussionsFetchSuccess(
            val discussionsResponse: DiscussionsResponse,
            val rootComments: List<Comment>
        ) : InternalMessage

        data class DiscussionRepliesFetchError(val discussionId: Long) : InternalMessage
        data class DiscussionRepliesFetchSuccess(
            val discussionId: Long,
            val replies: List<Comment>
        ) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data class FetchDiscussions(val stepId: Long, val page: Int) : InternalAction

        data class FetchDiscussionReplies(val discussionId: Long, val repliesIds: List<Long>) : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}