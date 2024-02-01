import SwiftUI

extension PaywallView {
    struct Appearance {
        let interitemSpacing = LayoutInsets.smallInset

        let illustrationMaxHeight: CGFloat = 164

        let maxWidth: CGFloat = DeviceInfo.current.isPad ? 400 : .infinity
    }
}

struct PaywallView: View {
    private(set) var appearance = Appearance()

    private let actionButtonsFeedbackGenerator = FeedbackGenerator(feedbackType: .selection)

    @Environment(\.presentationMode) var presentationMode

    var body: some View {
        VerticalCenteredScrollView {
            VStack(alignment: .leading, spacing: 0) {
                header
                PaywallFeaturesView()
                Spacer()
                actionButtons
            }
            .padding()
            .frame(maxWidth: appearance.maxWidth)
        }
    }

    @ViewBuilder private var header: some View {
        Image(.paywallPremiumMobile)
            .renderingMode(.original)
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(maxWidth: .infinity, maxHeight: appearance.illustrationMaxHeight)

        Text("Solve unlimited problems with Mobile only plan")
            .font(.title.bold())
            .foregroundColor(.newPrimaryText)
            .padding(.vertical)
    }

    @MainActor private var actionButtons: some View {
        VStack(alignment: .center, spacing: appearance.interitemSpacing) {
            Button(
                "Subscribe for $12.00/month",
                action: {
                    actionButtonsFeedbackGenerator.triggerFeedback()
                    presentationMode.wrappedValue.dismiss()
                }
            )
            .buttonStyle(.primary)
            .shineEffect()

            Button(
                "Continue with daily limits ",
                action: {
                    actionButtonsFeedbackGenerator.triggerFeedback()
                    presentationMode.wrappedValue.dismiss()
                }
            )
            .buttonStyle(GhostButtonStyle())
        }
    }
}

#Preview {
    PaywallView()
}
