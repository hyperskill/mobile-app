import Foundation
import shared

final class CommentsViewModel: FeatureViewModel<
  CommentsScreenViewState,
  CommentsScreenFeatureMessage,
  CommentsScreenFeatureActionViewAction
> {
    var discussionsViewStateKs: CommentsScreenViewStateDiscussionsViewStateKs { .init(state.discussions) }

    init(feature: Presentation_reduxFeature) {
        super.init(feature: feature)
        onNewMessage(CommentsScreenFeatureMessageInitialize())
    }

    override func shouldNotifyStateDidChange(
        oldState: CommentsScreenViewState,
        newState: CommentsScreenViewState
    ) -> Bool {
        !oldState.isEqual(newState)
    }

    func doRetryLoadCommentsScreen() {
        onNewMessage(CommentsScreenFeatureMessageRetryContentLoading())
    }

    func doShowDiscussionReplies(discussionID: Int64) {
        onNewMessage(CommentsScreenFeatureMessageShowDiscussionRepliesClicked(discussionId: discussionID))
    }

    func doShowMoreDiscussions() {
        onNewMessage(CommentsScreenFeatureMessageShowMoreDiscussionsClicked())
    }

    func doReactionMainAction(commentID: Int64, reactionType: ReactionType) {
        onNewMessage(CommentsScreenFeatureMessageReactionClicked(commentId: commentID, reactionType: reactionType))
    }

    func logViewedEvent() {
        onNewMessage(CommentsScreenFeatureMessageViewedEventMessage())
    }
}
