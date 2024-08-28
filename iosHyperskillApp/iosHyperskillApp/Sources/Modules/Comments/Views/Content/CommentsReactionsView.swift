import shared
import SwiftUI

struct CommentsReactionsView: View {
    let reactions: [CommentReaction]

    let onReactionTap: (ReactionType) -> Void

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: LayoutInsets.smallInset) {
                ForEach(reactions, id: \.reactionType) { reaction in
                    reactionView(
                        reaction,
                        action: {
                            onReactionTap(reaction.reactionType)
                        }
                    )
                }

                reactionsMenu(action: onReactionTap)
            }
        }
        .scrollBounceBehaviorBasedOnSize()
    }

    @ViewBuilder
    private func reactionView(
        _ commentReaction: CommentReaction,
        action: @escaping () -> Void
    ) -> some View {
        Button(action: action) {
            Text("\(commentReaction.emoji) \(commentReaction.value)")
                .font(.footnote)
                .foregroundColor(.newPrimaryText)
                .padding(LayoutInsets.smallInset)
                .background(commentReaction.backgroundColor)
                .clipShape(Capsule())
        }
        .buttonStyle(BounceButtonStyle())
    }

    @ViewBuilder
    private func reactionsMenu(action: @escaping (ReactionType) -> Void) -> some View {
        Menu(
            content: {
                ForEach(
                    Array(ReactionType.companion.commentReactions),
                    id: \.self
                ) { commentReaction in
                    Button(
                        action: {
                            action(commentReaction)
                        },
                        label: {
                            Text(commentReaction.menuItemText)
                        }
                    )
                }
            },
            label: {
                Image(.commentsReactions)
                    .renderingMode(.template)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(widthHeight: 24)
                    .foregroundColor(Color(ColorPalette.newButtonPrimary))
            }
        )
        .padding(.leading, LayoutInsets.smallInset)
    }
}

private extension ReactionType {
    // swiftlint:disable switch_case_on_newline
    var emoji: String {
        switch self {
        case .smile: "😄"
        case .plus: "👍"
        case .minus: "👎"
        case .confused: "😕"
        case .thinking: "🤔"
        case .fire: "🔥"
        case .clap: "👏"
        default: ""
        }
    }

    var emojiText: String {
        switch self {
        case .smile: "Haha"
        case .plus: "Like"
        case .minus: "Dislike"
        case .confused: "Confused"
        case .thinking: "Hmm"
        case .fire: "Awesome"
        case .clap: "Applause"
        default: ""
        }
    }
    // swiftlint:enable switch_case_on_newline

    var menuItemText: String { "\(emoji) \(emojiText)" }
}

private extension CommentReaction {
    var emoji: String { reactionType.emoji }

    var backgroundColor: Color {
        if isSet {
            Color(ColorPalette.primaryAlpha38)
        } else {
            Color.systemTertiaryGroupedBackground
        }
    }
}

#if DEBUG
#Preview {
    CommentsReactionsView(
        reactions: [
            CommentReaction(reactionType: .plus, value: 1, isSet: true),
            CommentReaction(reactionType: .fire, value: 2, isSet: false)
        ],
        onReactionTap: { _ in }
    )
    .padding()
}
#endif
