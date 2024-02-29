import SwiftUI

extension SubscriptionDetailsView {
    enum Appearance {
        static let spacing = LayoutInsets.defaultInset + LayoutInsets.smallInset
        static let interItemSpacing = LayoutInsets.smallInset

        static let noticeBadgeAppearance = BadgeView.Appearance(
            cornerRadius: LayoutInsets.defaultInset / 2,
            insets: .default,
            font: .body
        )
    }
}

struct SubscriptionDetailsView: View {
    var body: some View {
        ScrollView(.vertical, showsIndicators: false) {
            VStack(spacing: Appearance.spacing) {
                header
                details
                notice
            }
            .padding()
        }
        .scrollBounceBehaviorBasedOnSize()
        .safeAreaInsetBottomCompatibility(footer)
    }

    private var header: some View {
        VStack(alignment: .leading, spacing: Appearance.interItemSpacing) {
            Text("Your current plan:")
                .font(.callout)
            Text("Hyperskill Mobile only")
                .font(.title.bold())
            Text("Valid until January 27, 2024, 02:00")
                .font(.body)
        }
        .foregroundColor(.newPrimaryText)
        .frame(maxWidth: .infinity, alignment: .leading)
    }

    private var details: some View {
        VStack(alignment: .leading, spacing: Appearance.interItemSpacing) {
            Text("Plan details:")
                .font(.callout.bold())
            PaywallFeaturesView()
        }
    }

    private var notice: some View {
        BadgeView(
            appearance: Appearance.noticeBadgeAppearance,
            text: """
Please be aware that with the Mobile оnly plan on hyperskill.org, \
there is a limit on the number of problems you can solve per day.
""",
            style: .blue
        )
    }

    @MainActor private var footer: some View {
        Button(
            "Manage subscription",
            action: {
                FeedbackGenerator(feedbackType: .selection).triggerFeedback()
            }
        )
        .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
        .padding()
        .background(
            TransparentBlurView()
                .edgesIgnoringSafeArea(.all)
        )
        .fixedSize(horizontal: false, vertical: true)
    }
}

#Preview {
    SubscriptionDetailsView()
}
