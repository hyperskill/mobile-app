package org.hyperskill.app.comments.presentation

import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Action
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Message
import org.hyperskill.app.comments.screen.view.model.CommentsScreenViewState
import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.reactions.domain.model.ReactionType

class CommentsViewModel(
    viewContainer: FlowView<CommentsScreenViewState, Message, Action.ViewAction>
) : ReduxFlowViewModel<CommentsScreenViewState, Message, Action.ViewAction>(viewContainer) {
    init {
        onNewMessage(Message.Initialize)
    }

    fun onRetryClick() {
        onNewMessage(Message.RetryContentLoading)
    }

    fun onShowRepliesClick(discussionId: Long) {
        onNewMessage(Message.ShowDiscussionRepliesClicked(discussionId))
    }

    fun onReactionClick(commentId: Long, reactionType: ReactionType) {
        onNewMessage(
            Message.ReactionClicked(
                commentId = commentId,
                reactionType = reactionType
            )
        )
    }

    fun onShowMoreDiscussionsClick() {
        onNewMessage(Message.ShowMoreDiscussionsClicked)
    }
}