import SwiftUI

extension PaywallFooterView {
    struct Appearance {
        var spacing = LayoutInsets.defaultInset
    }
}

struct PaywallFooterView: View {
    private(set) var appearance = Appearance()

    let buyButtonText: String
    let isContinueWithLimitsButtonVisible: Bool

    let onBuyButtonTap: () -> Void
    let onContinueWithLimitsButtonTap: () -> Void
    let onTermsOfServiceButtonTap: () -> Void

    private let feedbackGenerator = FeedbackGenerator(feedbackType: .selection)

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
                buyButtonText,
                action: {
                    feedbackGenerator.triggerFeedback()
                    onBuyButtonTap()
                }
            )
            .buttonStyle(.primary)
            .shineEffect()

            if isContinueWithLimitsButtonVisible {
                Button(
                    Strings.Paywall.continueWithLimitsButton,
                    action: {
                        feedbackGenerator.triggerFeedback()
                        onContinueWithLimitsButtonTap()
                    }
                )
                .buttonStyle(OutlineButtonStyle(style: .newGray))
            }

            Button(
                Strings.Paywall.termsOfServiceButton,
                action: {
                    feedbackGenerator.triggerFeedback()
                    onTermsOfServiceButtonTap()
                }
            )
            .font(.footnote)
            .foregroundColor(.newSecondaryText)
        }
    }
}

#if DEBUG
#Preview {
    VStack {
        PaywallFooterView(
            buyButtonText: "Subscribe for $11.99/month",
            isContinueWithLimitsButtonVisible: true,
            onBuyButtonTap: {},
            onContinueWithLimitsButtonTap: {},
            onTermsOfServiceButtonTap: {}
        )

        PaywallFooterView(
            buyButtonText: "Subscribe for $11.99/month",
            isContinueWithLimitsButtonVisible: false,
            onBuyButtonTap: {},
            onContinueWithLimitsButtonTap: {},
            onTermsOfServiceButtonTap: {}
        )
    }
}
#endif
