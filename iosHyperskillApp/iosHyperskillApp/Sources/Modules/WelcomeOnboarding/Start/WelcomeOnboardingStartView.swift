import SwiftUI

extension WelcomeOnboardingStartView {
    struct Appearance {
        let interitemSpacing = LayoutInsets.smallInset

        let illustrationMaxHeight: CGFloat = 380

        let maxWidth: CGFloat = DeviceInfo.current.isPad ? 400 : .infinity
    }
}

struct WelcomeOnboardingStartView: View {
    private(set) var appearance = Appearance()

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    let onCallToActionButtonTap: () -> Void

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
            Text(Strings.WelcomeOnboarding.StartScreen.title)
                .font(.title).bold()
                .foregroundColor(.newPrimaryText)

            Text(Strings.WelcomeOnboarding.StartScreen.description)
                .font(.body)
                .foregroundColor(.newSecondaryText)
        }
        .multilineTextAlignment(.center)
    }

    private var illustration: some View {
        Image(.firstProblemOnboardingNewUserIllustration)
            .renderingMode(.original)
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(maxWidth: .infinity, maxHeight: appearance.illustrationMaxHeight)
    }

    @MainActor private var actionButton: some View {
        Button(
            Strings.WelcomeOnboarding.StartScreen.startButton,
            action: {
                FeedbackGenerator(feedbackType: .selection).triggerFeedback()
                onCallToActionButtonTap()
            }
        )
        .buttonStyle(.primary)
        .shineEffect()
    }
}

#if DEBUG
#Preview {
    WelcomeOnboardingStartView(onCallToActionButtonTap: {})
}
#endif
