import shared
import SwiftUI

extension CommentsContentView {
    enum Appearance {
        static let replyLeadingInset = LayoutInsets.defaultInset * 2
    }
}

struct CommentsContentView: View {
    let viewData: CommentsScreenViewStateDiscussionsViewStateContent

    let onShowDiscussionRepliesButtonTap: (Int64) -> Void
    let onShowMoreDiscussionsButtonTap: () -> Void

    let onReactionButtonTap: (Int64, ReactionType) -> Void

    var body: some View {
        List {
            ForEach(viewData.discussions) { discussion in
                Section {
                    CommentsCommentView(
                        comment: discussion.comment,
                        onReactionTap: { reaction in
                            onReactionButtonTap(discussion.id.int64Value, reaction)
                        }
                    )

                    switch CommentsScreenViewStateDiscussionRepliesKs(discussion.replies) {
                    case .emptyReplies:
                        EmptyView()
                    case .showRepliesButton:
                        actionButton(
                            title: Strings.Comments.showRepliesButton,
                            action: {
                                onShowDiscussionRepliesButtonTap(discussion.id.int64Value)
                            },
                            alignment: .leading
                        )
                    case .loadingReplies:
                        CommentsCommentSkeletonView()
                            .padding(.leading, Appearance.replyLeadingInset)
                    case .content(let data):
                        ForEach(data.replies) { reply in
                            CommentsCommentView(
                                comment: reply,
                                onReactionTap: { reaction in
                                    onReactionButtonTap(reply.id.int64Value, reaction)
                                }
                            )
                        }
                        .padding(.leading, Appearance.replyLeadingInset)
                    }

                    if viewData.discussions.isLastItem(discussion) && viewData.hasNextPage {
                        Divider()

                        if viewData.isLoadingNextPage {
                            CommentsCommentSkeletonView()
                        } else {
                            actionButton(
                                title: Strings.Comments.showMoreButton,
                                action: onShowMoreDiscussionsButtonTap
                            )
                        }
                    } else {
                        Divider()
                    }
                }
                .listRowSeparatorHidden()
            }
        }
        .listStyle(.plain)
    }

    private func actionButton(
        title: String,
        action: @escaping () -> Void,
        alignment: Alignment = .center
    ) -> some View {
        Button(title, action: action)
            .buttonStyle(.plain)
            .foregroundColor(Color(ColorPalette.newButtonPrimary))
            .frame(maxWidth: .infinity, alignment: alignment)
    }
}

extension CommentsScreenViewState.DiscussionItem: Identifiable {}

extension CommentsScreenViewState.CommentItem: Identifiable {}

extension CommentsCommentView {
    init(
        comment: CommentsScreenViewState.CommentItem,
        onReactionTap: @escaping (ReactionType) -> Void
    ) {
        self.init(
            authorAvatar: comment.authorAvatar,
            authorFullName: comment.authorFullName,
            formattedTime: comment.formattedTime,
            text: comment.text,
            reactions: comment.reactions,
            onReactionTap: onReactionTap
        )
    }
}

#if DEBUG
#Preview {
    CommentsContentView(
        viewData: .placeholder(hasNextPage: true, isLoadingNextPage: false),
        onShowDiscussionRepliesButtonTap: { _ in },
        onShowMoreDiscussionsButtonTap: {}, 
        onReactionButtonTap: { _,_  in }
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
                        text: text,
                        reactions: [
                            CommentReaction(reactionType: .plus, value: 1, isSet: true),
                            CommentReaction(reactionType: .fire, value: 2, isSet: false)
                        ]
                    ),
                    replies: CommentsScreenViewStateDiscussionRepliesShowRepliesButton()
                ),
                CommentsScreenViewState.DiscussionItem(
                    comment: CommentsScreenViewState.CommentItem(
                        id: 2,
                        authorAvatar: "",
                        authorFullName: "mantraolympics",
                        formattedTime: "10 months ago",
                        text: text,
                        reactions: []
                    ),
                    replies: CommentsScreenViewStateDiscussionRepliesEmptyReplies()
                ),
                CommentsScreenViewState.DiscussionItem(
                    comment: CommentsScreenViewState.CommentItem(
                        id: 3,
                        authorAvatar: "",
                        authorFullName: "mantraolympics",
                        formattedTime: "2 years ago",
                        text: text,
                        reactions: []
                    ),
                    replies: CommentsScreenViewStateDiscussionRepliesContent(
                        replies: [
                            CommentsScreenViewState.CommentItem(
                                id: 4,
                                authorAvatar: "",
                                authorFullName: "mantraolympics",
                                formattedTime: "10 years ago",
                                text: "The same for me",
                                reactions: []
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
