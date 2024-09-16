import SwiftUI

extension PaywallFooterView {
    struct Appearance {
        var spacing = LayoutInsets.defaultInset
    }
}

struct PaywallFooterView: View {
    private(set) var appearance = Appearance()

    let buyButtonText: String

    let onBuyButtonTap: () -> Void
    let onTermsOfServiceButtonTap: () -> Void

    private let feedbackGenerator = FeedbackGenerator(feedbackType: .selection)

    var body: some View {
        content
            .padding()
            .background(
                TransparentBlurView()
                    .edgesIgnoringSafeArea(.all)
            )
            .fixedSize(horizontal: false, vertical: true)
    }

    @MainActor private var content: some View {
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
    PaywallFooterView(
        buyButtonText: "Subscribe for $11.99/month",
        onBuyButtonTap: {},
        onTermsOfServiceButtonTap: {}
    )
}
#endif
