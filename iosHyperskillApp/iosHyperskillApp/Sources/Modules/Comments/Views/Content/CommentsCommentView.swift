import shared
import SwiftUI

extension CommentsCommentView {
    struct Appearance {
        let avatarSize = CGSize(width: 40, height: 40)
    }
}

struct CommentsCommentView: View {
    private(set) var appearance = Appearance()

    let authorAvatar: String
    let authorFullName: String
    let formattedTime: String?
    let text: String
    let reactions: [CommentReaction]

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            author

            Text(text)
                .font(.body)
                .foregroundColor(.newPrimaryText)
                .frame(maxWidth: .infinity, alignment: .leading)

            CommentsReactionsView(
                reactions: reactions,
                onReactionTap: { _ in }
            )
        }
    }

    private var author: some View {
        HStack {
            LazyAvatarView(authorAvatar)
                .frame(size: appearance.avatarSize)

            VStack(alignment: .leading) {
                Text(authorFullName)
                    .font(.headline)
                    .foregroundColor(.newSecondaryText)

                Text(formattedTime)
                    .font(.body)
                    .foregroundColor(.newSecondaryText)
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

#if DEBUG
#Preview {
    CommentsCommentView(
        authorAvatar: "",
        authorFullName: "mantraolympics",
        formattedTime: "10 months ago",
        text: """
Which version of python are you using? In python 3, the type function returns the <class data_type> format.
""",
        reactions: [
            CommentReaction(reactionType: .plus, value: 1, isSet: true),
            CommentReaction(reactionType: .fire, value: 2, isSet: false)
        ]
    )
    .padding()
}
#endif
