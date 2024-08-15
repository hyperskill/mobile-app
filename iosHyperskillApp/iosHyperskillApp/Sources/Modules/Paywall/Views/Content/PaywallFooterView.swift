import SwiftUI

extension PaywallFooterView {
    struct Appearance {
        var spacing = LayoutInsets.defaultInset
        var interitemSpacing = LayoutInsets.smallInset
    }
}

struct PaywallFooterView: View {
    private(set) var appearance = Appearance()

    let buyButtonText: String
    let buyFootnoteText: String?

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
            VStack(alignment: .center, spacing: appearance.interitemSpacing) {
                Button(
                    buyButtonText,
                    action: {
                        feedbackGenerator.triggerFeedback()
                        onBuyButtonTap()
                    }
                )
                .buttonStyle(.primary)
                .shineEffect()

                if let buyFootnoteText {
                    Text(buyFootnoteText)
                        .font(.footnote.bold())
                        .foregroundColor(.newSecondaryText)
                }
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
            buyFootnoteText: nil,
            onBuyButtonTap: {},
            onTermsOfServiceButtonTap: {}
        )

        PaywallFooterView(
            buyButtonText: "Subscribe for $11.99/month",
            buyFootnoteText: "Then $11.99 per month",
            onBuyButtonTap: {},
            onTermsOfServiceButtonTap: {}
        )
    }
}
#endif
