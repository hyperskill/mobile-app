import shared
import SwiftUI

extension StepQuizFeedbackWrongStateView {
    struct Appearance {
        let feedbackStatusAppearance = StepQuizFeedbackStatusView.Appearance()
    }
}

struct StepQuizFeedbackWrongStateView: View {
    private(set) var appearance = Appearance()

    let state: StepQuizFeedbackStateWrong

    let onAction: (StepQuizFeedbackStateWrong.Action) -> Void

    var body: some View {
        HStack(
            alignment: .firstTextBaseline,
            spacing: appearance.feedbackStatusAppearance.interItemSpacing
        ) {
            Image(systemName: "xmark")
                .foregroundColor(Color(ColorPalette.overlayOrange))
                .font(appearance.feedbackStatusAppearance.font)

            VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
                Text(state.title)
                    .foregroundColor(Color(ColorPalette.overlayOrange))
                    .font(appearance.feedbackStatusAppearance.font)

                if let description = state.description_ {
                    Text(description)
                        .font(.subheadline)
                        .foregroundColor(.primaryText)
                }

                if let actionType = state.actionType,
                   let actionText = state.actionText {
                    Button(
                        action: {
                            onAction(actionType)
                        },
                        label: {
                            Text(actionText)
                                .font(.subheadline.weight(.semibold))
                                .foregroundColor(.primaryText)
                                .underline()
                        }
                    )
                }
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
        .background(Color(ColorPalette.overlayOrangeAlpha12))
        .cornerRadius(appearance.feedbackStatusAppearance.cornerRadius)
    }
}

#if DEBUG
let previewTitle = "Not correct"

#Preview("Not correct") {
    StepQuizFeedbackWrongStateView(
        state: StepQuizFeedbackStateWrong(
            title: previewTitle,
            description: nil,
            actionText: nil,
            actionType: nil,
            hint: nil
        ),
        onAction: { _ in }
    )
    .padding()
}

#Preview("See community hint") {
    StepQuizFeedbackWrongStateView(
        state: StepQuizFeedbackStateWrong(
            title: previewTitle,
            description: "Take a look at what other users are suggesting to solve this problem.",
            actionText: "See community hint",
            actionType: nil,
            hint: nil
        ),
        onAction: { _ in }
    )
    .padding()
}
#endif
