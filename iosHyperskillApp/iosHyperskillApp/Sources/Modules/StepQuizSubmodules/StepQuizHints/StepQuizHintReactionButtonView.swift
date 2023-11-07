import shared
import SwiftUI

extension StepQuizHintReactionButtonView {
    struct Appearance {
        let reactionImageWidthHeight: CGFloat = 16
        let buttonHorizontalPadding: CGFloat = 12
        let buttonVerticalPadding: CGFloat = 4
    }
}

struct StepQuizHintReactionButtonView: View {
    private(set) var appearance = Appearance()

    let reactionImage: String

    let reactionText: String

    let onReactionButtonTap: () -> Void

    var body: some View {
        Button(
            action: onReactionButtonTap,
            label: {
                HStack(spacing: LayoutInsets.smallInset) {
                    Image(reactionImage)
                        .renderingMode(.original)
                        .resizable()
                        .frame(widthHeight: appearance.reactionImageWidthHeight)

                    Text(reactionText)
                        .font(.subheadline)
                        .foregroundColor(Color(ColorPalette.primary))
                }
                .padding(.horizontal, appearance.buttonHorizontalPadding)
                .padding(.vertical, appearance.buttonVerticalPadding)
            }
        )
        .buttonStyle(BounceButtonStyle(bounceScale: 0.75))
    }
}

struct StepQuizHintReactionButtonView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizHintReactionButtonView(
            reactionImage: Images.StepQuiz.Hints.unhelpfulReaction,
            reactionText: Strings.Common.no,
            onReactionButtonTap: {}
        )
    }
}
