import shared
import SwiftUI

struct CommentsView: View {
    @StateObject var viewModel: CommentsViewModel

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            buildBody()
                .animation(.default, value: viewModel.state)
        }
        .onAppear {
            viewModel.onViewAction = handleViewAction(_:)
            viewModel.startListening()
        }
        .onDisappear {
            viewModel.onViewAction = nil
            viewModel.stopListening()
        }
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.discussionsViewStateKs {
        case .idle, .loading:
            CommentsSkeletonView()
        case .error:
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: .clear,
                    action: viewModel.doRetryLoadCommentsScreen
                )
            )
        case .content(let viewData):
            CommentsContentView(
                viewData: viewData,
                onShowDiscussionRepliesButtonTap: viewModel.doShowDiscussionReplies(discussionID:),
                onShowMoreDiscussionsButtonTap: viewModel.doShowMoreDiscussions,
                onReactionButtonTap: viewModel.doReactionMainAction(commentID:reactionType:)
            )
        }
    }
}

// MARK: - CommentsView (ViewAction) -

private extension CommentsView {
    func handleViewAction(
        _ viewAction: CommentsScreenFeatureActionViewAction
    ) {
        assertionFailure("Unknown view action: \(viewAction)")
    }
}
