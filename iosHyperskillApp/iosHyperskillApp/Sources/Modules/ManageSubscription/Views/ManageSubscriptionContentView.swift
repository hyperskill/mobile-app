import SwiftUI

extension ManageSubscriptionContentView {
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

struct ManageSubscriptionContentView: View {
    let validUntilText: String?

    let callToActionButtonText: String?
    let onCallToActionButtonTap: () -> Void

    var body: some View {
        ScrollView(.vertical, showsIndicators: false) {
            VStack(spacing: Appearance.spacing) {
                header
                details
                BadgeView(
                    appearance: Appearance.noticeBadgeAppearance,
                    text: Strings.ManageSubscription.mobileOnlyWarning,
                    style: .blue
                )
            }
            .padding()
        }
        .scrollBounceBehaviorBasedOnSize()
        .safeAreaInsetBottomCompatibility(footer)
    }

    private var header: some View {
        VStack(alignment: .leading, spacing: Appearance.interItemSpacing) {
            Text(Strings.ManageSubscription.planTitle)
                .font(.callout)
            Text(Strings.ManageSubscription.mobileOnly)
                .font(.title.bold())

            if let validUntilText {
                Text(validUntilText)
                    .font(.body)
            }
        }
        .foregroundColor(.newPrimaryText)
        .frame(maxWidth: .infinity, alignment: .leading)
    }

    private var details: some View {
        VStack(alignment: .leading, spacing: Appearance.interItemSpacing) {
            Text(Strings.ManageSubscription.planDetailsTitle)
                .font(.callout.bold())
            PaywallFeaturesView()
        }
    }

    @MainActor @ViewBuilder private var footer: some View {
        if let callToActionButtonText {
            Button(
                callToActionButtonText,
                action: {
                    FeedbackGenerator(feedbackType: .selection).triggerFeedback()
                    onCallToActionButtonTap()
                }
            )
            .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
            .padding()
            .background(
                TransparentBlurView()
                    .edgesIgnoringSafeArea(.all)
            )
            .fixedSize(horizontal: false, vertical: true)
        } else {
            EmptyView()
        }
    }
}

#if DEBUG
#Preview {
    ManageSubscriptionContentView(
        validUntilText: "Valid until January 27, 2024, 02:00",
        callToActionButtonText: "Manage subscription",
        onCallToActionButtonTap: {}
    )
}

#Preview {
    ManageSubscriptionContentView(
        validUntilText: nil,
        callToActionButtonText: nil,
        onCallToActionButtonTap: {}
    )
}
#endif
