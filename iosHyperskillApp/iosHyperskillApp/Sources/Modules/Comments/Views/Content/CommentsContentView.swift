import shared
import SwiftUI

struct CommentsContentView: View {
    let viewData: CommentsScreenViewStateDiscussionsViewStateContent

    let onShowDiscussionRepliesButtonTap: (Int64) -> Void
    let onShowMoreDiscussionsButtonTap: () -> Void

    var body: some View {
        List {
            ForEach(viewData.discussions) { discussion in
                Section {
                    CommentsCommentView(comment: discussion.comment)

                    switch CommentsScreenViewStateDiscussionRepliesKs(discussion.replies) {
                    case .emptyReplies:
                        EmptyView()
                    case .showRepliesButton:
                        Button(
                            "Show replies",
                            action: {
                                onShowDiscussionRepliesButtonTap(discussion.id.int64Value)
                            }
                        )
                    case .loadingReplies:
                        ProgressView()
                    case .content(let data):
                        ForEach(data.replies) { reply in
                            CommentsCommentView(comment: reply)
                                .padding(.leading)
                        }
                        .padding(.leading)
                    }

                    if viewData.discussions.isLastItem(discussion) && viewData.hasNextPage {
                        if viewData.isLoadingNextPage {
                            ProgressView()
                        } else {
                            Button("Show more", action: onShowMoreDiscussionsButtonTap)
                        }
                    }
                }
            }
        }
        .listStyle(.plain)
    }
}

extension CommentsScreenViewState.DiscussionItem: Identifiable {}

extension CommentsScreenViewState.CommentItem: Identifiable {}

extension CommentsCommentView {
    init(comment: CommentsScreenViewState.CommentItem) {
        self.init(
            authorAvatar: comment.authorAvatar,
            authorFullName: comment.authorFullName,
            formattedTime: comment.formattedTime,
            text: comment.text
        )
    }
}

#if DEBUG
#Preview {
    CommentsContentView(
        viewData: .placeholder(hasNextPage: true, isLoadingNextPage: false),
        onShowDiscussionRepliesButtonTap: { _ in },
        onShowMoreDiscussionsButtonTap: {}
    )
}

extension CommentsScreenViewStateDiscussionsViewStateContent {
    static func placeholder(
        hasNextPage: Bool,
        isLoadingNextPage: Bool
    ) -> CommentsScreenViewStateDiscussionsViewStateContent {
        let text = """
Which version of python are you using? In python 3, the type function returns the <class data_type> format.
"""

        return CommentsScreenViewStateDiscussionsViewStateContent(
            discussions: [
                CommentsScreenViewState.DiscussionItem(
                    comment: CommentsScreenViewState.CommentItem(
                        id: 1,
                        authorAvatar: "",
                        authorFullName: "mantraolympics",
                        formattedTime: "a month ago",
                        text: text
                    ),
                    replies: CommentsScreenViewStateDiscussionRepliesShowRepliesButton()
                ),
                CommentsScreenViewState.DiscussionItem(
                    comment: CommentsScreenViewState.CommentItem(
                        id: 2,
                        authorAvatar: "",
                        authorFullName: "mantraolympics",
                        formattedTime: "10 months ago",
                        text: text
                    ),
                    replies: CommentsScreenViewStateDiscussionRepliesEmptyReplies()
                ),
                CommentsScreenViewState.DiscussionItem(
                    comment: CommentsScreenViewState.CommentItem(
                        id: 3,
                        authorAvatar: "",
                        authorFullName: "mantraolympics",
                        formattedTime: "2 years ago",
                        text: text
                    ),
                    replies: CommentsScreenViewStateDiscussionRepliesContent(
                        replies: [
                            CommentsScreenViewState.CommentItem(
                                id: 4,
                                authorAvatar: "",
                                authorFullName: "mantraolympics",
                                formattedTime: "10 years ago",
                                text: "The same for me"
                            )
                        ]
                    )
                )
            ],
            hasNextPage: hasNextPage,
            isLoadingNextPage: isLoadingNextPage
        )
    }
}
#endif
