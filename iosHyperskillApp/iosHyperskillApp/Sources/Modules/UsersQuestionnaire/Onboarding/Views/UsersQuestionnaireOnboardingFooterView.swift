import SwiftUI

extension UsersQuestionnaireOnboardingFooterView {
    struct Appearance {
        var spacing = LayoutInsets.smallInset
    }
}

struct UsersQuestionnaireOnboardingFooterView: View {
    private(set) var appearance = Appearance()

    private let feedbackGenerator = FeedbackGenerator(feedbackType: .selection)

    let isSendButtonEnabled: Bool

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
                    onSendButtotTap()
                }
            )
            .buttonStyle(.primary)
            .shineEffect(isActive: isSendButtonEnabled)
            .disabled(!isSendButtonEnabled)

            Button(
                Strings.QuestionnaireOnboarding.skipButton,
                action: {
                    feedbackGenerator.triggerFeedback()
                    onSkipButtotTap()
                }
            )
            .buttonStyle(GhostButtonStyle())
        }
    }
}

#if DEBUG
#Preview {
    VStack {
        UsersQuestionnaireOnboardingFooterView(
            isSendButtonEnabled: true,
            onSendButtotTap: {},
            onSkipButtotTap: {}
        )

        UsersQuestionnaireOnboardingFooterView(
            isSendButtonEnabled: false,
            onSendButtotTap: {},
            onSkipButtotTap: {}
        )
    }
}
#endif
