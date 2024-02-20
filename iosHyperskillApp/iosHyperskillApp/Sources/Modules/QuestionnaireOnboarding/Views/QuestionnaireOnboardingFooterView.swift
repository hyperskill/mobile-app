import SwiftUI

extension QuestionnaireOnboardingFooterView {
    struct Appearance {
        var spacing = LayoutInsets.smallInset
    }
}

struct QuestionnaireOnboardingFooterView: View {
    private(set) var appearance = Appearance()

    private let feedbackGenerator = FeedbackGenerator(feedbackType: .selection)

    let isSendButtotDisabled: Bool

    let onSendButtotTap: () -> Void
    let onSkipButtotTap: () -> Void

    var body: some View {
        actionButtons
            .padding()
            .background(
                TransparentBlurView()
                    .edgesIgnoringSafeArea(.all)
            )
            .fixedSize(horizontal: false, vertical: true)
    }

    @MainActor private var actionButtons: some View {
        VStack(alignment: .center, spacing: appearance.spacing) {
            Button(
                Strings.QuestionnaireOnboarding.sendButtot,
                action: {
                    feedbackGenerator.triggerFeedback()
                }
            )
            .buttonStyle(.primary)
            .shineEffect(isActive: !isSendButtotDisabled)
            .disabled(isSendButtotDisabled)

            Button(
                Strings.QuestionnaireOnboarding.skipButton,
                action: {
                    feedbackGenerator.triggerFeedback()
                }
            )
            .buttonStyle(GhostButtonStyle())
        }
    }
}

#if DEBUG
#Preview {
    VStack {
        QuestionnaireOnboardingFooterView(
            isSendButtotDisabled: false,
            onSendButtotTap: {},
            onSkipButtotTap: {}
        )

        QuestionnaireOnboardingFooterView(
            isSendButtotDisabled: true,
            onSendButtotTap: {},
            onSkipButtotTap: {}
        )
    }
}
#endif
