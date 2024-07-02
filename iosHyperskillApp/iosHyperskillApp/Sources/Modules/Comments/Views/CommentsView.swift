import shared
import SwiftUI

extension CommentsView {
    struct Appearance {
        let backgroundColor = Color.background
    }
}

struct CommentsView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: CommentsViewModel

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: appearance.backgroundColor)

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
            ProgressView()
        case .error:
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: appearance.backgroundColor,
                    action: viewModel.doRetryLoadCommentsScreen
                )
            )
        case .content(let viewData):
            CommentsContentView(
                viewData: viewData,
                onShowDiscussionRepliesButtonTap: viewModel.doShowDiscussionReplies(discussionID:),
                onShowMoreDiscussionsButtonTap: viewModel.doShowMoreDiscussions
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
