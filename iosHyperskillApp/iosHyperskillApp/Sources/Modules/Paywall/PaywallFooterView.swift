import SwiftUI

extension PaywallFooterView {
    struct Appearance {
        var spacing = LayoutInsets.defaultInset
    }
}

struct PaywallFooterView: View {
    private(set) var appearance = Appearance()

    private let feedbackGenerator = FeedbackGenerator(feedbackType: .selection)

    @Environment(\.presentationMode) var presentationMode

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
                "Subscribe for $12.00/month",
                action: {
                    feedbackGenerator.triggerFeedback()
                    presentationMode.wrappedValue.dismiss()
                }
            )
            .buttonStyle(.primary)
            .shineEffect()

            Button(
                "Continue with daily limits ",
                action: {
                    feedbackGenerator.triggerFeedback()
                    presentationMode.wrappedValue.dismiss()
                }
            )
            .buttonStyle(OutlineButtonStyle(style: .newGray))
            //.buttonStyle(GhostButtonStyle())

            Button(
                "Hyperskill Terms of Service and Privacy Policy",
                action: {
                    feedbackGenerator.triggerFeedback()
                    presentationMode.wrappedValue.dismiss()
                }
            )
            .font(.footnote)
            .foregroundColor(.newSecondaryText)
        }
    }
}

#Preview {
    PaywallFooterView()
}
