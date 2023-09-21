import SwiftUI

extension NotificationsOnboardingContentView {
    struct Appearance {
        let interitemSpacing = LayoutInsets.smallInset

        let illustrationHeight: CGFloat = 320

        let maxWidth: CGFloat = DeviceInfo.current.isPad ? 400 : .infinity

        let primaryButtonStyle = RoundedRectangleButtonStyle(style: .violet)
    }
}

struct NotificationsOnboardingContentView: View {
    private(set) var appearance = Appearance()

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    private let actionButtonsFeedbackGenerator = FeedbackGenerator(feedbackType: .selection)

    let onPrimaryButtonTap: () -> Void
    let onSecondaryButtonTap: () -> Void

    var body: some View {
        VerticalCenteredScrollView(showsIndicators: false) {
            VStack(spacing: 0) {
                if horizontalSizeClass == .regular {
                    Spacer()
                }

                VStack(spacing: 0) {
                    if horizontalSizeClass == .compact {
                        Spacer()
                    }
                    header
                        .padding(.vertical)
                    if horizontalSizeClass == .compact {
                        Spacer()
                    }

                    illustration
                        .padding(.vertical)
                    if horizontalSizeClass == .compact {
                        Spacer()
                    }

                    actionButtons
                        .padding(.top)
                }

                if horizontalSizeClass == .regular {
                    Spacer()
                }
            }
            .padding()
            .frame(maxWidth: appearance.maxWidth)
        }
    }

    private var header: some View {
        VStack(alignment: .center, spacing: appearance.interitemSpacing) {
            Text(Strings.NotificationsOnboarding.title)
                .font(.title).bold()
                .foregroundColor(.primaryText)

            Text(Strings.NotificationsOnboarding.subtitle)
                .font(.body)
                .foregroundColor(.secondaryText)
        }
        .multilineTextAlignment(.center)
    }

    private var illustration: some View {
        Image(Images.NotificationsOnboarding.illustration)
            .renderingMode(.original)
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(maxWidth: .infinity)
            .frame(maxHeight: appearance.illustrationHeight)
    }

    @MainActor
    private var actionButtons: some View {
        VStack(alignment: .center, spacing: appearance.interitemSpacing) {
            Button(
                Strings.NotificationsOnboarding.buttonPrimary,
                action: {
                    actionButtonsFeedbackGenerator.triggerFeedback()
                    onPrimaryButtonTap()
                }
            )
            .buttonStyle(appearance.primaryButtonStyle)
            .shineEffect()

            Button(
                Strings.NotificationsOnboarding.buttonSecondary,
                action: {
                    actionButtonsFeedbackGenerator.triggerFeedback()
                    onSecondaryButtonTap()
                }
            )
            .frame(
                maxWidth: .infinity,
                minHeight: appearance.primaryButtonStyle.minHeight,
                alignment: .center
            )
        }
    }
}

struct NotificationsOnboardingContentView_Previews: PreviewProvider {
    static var previews: some View {
        NotificationsOnboardingContentView(
            onPrimaryButtonTap: {},
            onSecondaryButtonTap: {}
        )
        .previewDevice(PreviewDevice(rawValue: "iPhone 14 Pro"))

        NotificationsOnboardingContentView(
            onPrimaryButtonTap: {},
            onSecondaryButtonTap: {}
        )
        .previewDevice(PreviewDevice(rawValue: "iPad (10th generation)"))
    }
}
