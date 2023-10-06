import SwiftUI

extension FirstProblemOnboardingContentView {
    struct Appearance {
        let interitemSpacing = LayoutInsets.smallInset

        let illustrationHeight: CGFloat = 320

        let maxWidth: CGFloat = DeviceInfo.current.isPad ? 400 : .infinity
    }
}

struct FirstProblemOnboardingContentView: View {
    private(set) var appearance = Appearance()

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    private let actionButtonsFeedbackGenerator = FeedbackGenerator(feedbackType: .selection)

    let isNewUserMode: Bool

    let subtitle: String

    let onLearningActionButtonTap: () -> Void

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

                    actionButton
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
                .foregroundColor(.newPrimaryText)

            Text(Strings.NotificationsOnboarding.subtitle)
                .font(.body)
                .foregroundColor(.newSecondaryText)
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
    private var actionButton: some View {
        VStack(alignment: .center, spacing: appearance.interitemSpacing) {
            Button(
                Strings.NotificationsOnboarding.buttonPrimary,
                action: {
                    actionButtonsFeedbackGenerator.triggerFeedback()
                    onPrimaryButtonTap()
                }
            )
            .buttonStyle(RoundedRectangleButtonStyle(style: .newViolet))
            .shineEffect()
        }
    }
}

struct FirstProblemOnboardingContentView_Previews: PreviewProvider {
    static var previews: some View {
        FirstProblemOnboardingContentView(
            onPrimaryButtonTap: {},
            onSecondaryButtonTap: {}
        )
        .previewDevice(PreviewDevice(rawValue: "iPhone 14 Pro"))

        FirstProblemOnboardingContentView(
            onPrimaryButtonTap: {},
            onSecondaryButtonTap: {}
        )
        .previewDevice(PreviewDevice(rawValue: "iPad (10th generation)"))
    }
}
