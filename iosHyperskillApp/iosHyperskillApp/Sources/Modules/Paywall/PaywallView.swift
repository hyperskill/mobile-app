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

    var body: some View {
        VerticalCenteredScrollView {
            VStack(alignment: .leading, spacing: 0) {
                header
                description
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
            .font(.title).bold()
            .foregroundColor(.newPrimaryText)
            .padding(.vertical)
    }

    private var description: some View {
        VStack(alignment: .leading, spacing: appearance.interitemSpacing) {
            ForEach(
                [
                    "Access to all tracks",
                    "Unlimited problems per day in the app",
                    "1 hint per problem"
                ],
                id: \.self
            ) { title in
                Label(
                    title: { Text(title) },
                    icon: {
                        Image(systemName: "checkmark")
                            .renderingMode(.template)
                            .foregroundColor(Color(ColorPalette.primary))
                    }
                )
                .font(.body)
            }
        }
    }

    @MainActor private var actionButtons: some View {
        VStack(alignment: .center, spacing: appearance.interitemSpacing) {
            Button(
                "Subscribe for $12.00/month",
                action: {
                    actionButtonsFeedbackGenerator.triggerFeedback()
                }
            )
            .buttonStyle(.primary)
            .shineEffect()

            Button(
                "Continue with daily limits ",
                action: {
                    actionButtonsFeedbackGenerator.triggerFeedback()
                }
            )
            .buttonStyle(GhostButtonStyle())
        }
    }
}

#Preview {
    PaywallView()
}
