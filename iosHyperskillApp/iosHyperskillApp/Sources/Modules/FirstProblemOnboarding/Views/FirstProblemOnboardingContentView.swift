import SwiftUI

extension FirstProblemOnboardingContentView {
    struct Appearance {
        let interitemSpacing = LayoutInsets.smallInset

        let illustrationMaxHeight: CGFloat = 380

        let maxWidth: CGFloat = DeviceInfo.current.isPad ? 400 : .infinity
    }
}

struct FirstProblemOnboardingContentView: View {
    private(set) var appearance = Appearance()

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    private let actionButtonsFeedbackGenerator = FeedbackGenerator(feedbackType: .selection)

    let title: String

    let subtitle: String

    let buttonText: String

    let isNewUserMode: Bool

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
            Text(title)
                .font(.title).bold()
                .foregroundColor(.newPrimaryText)

            Text(subtitle)
                .font(.body)
                .foregroundColor(.newSecondaryText)
        }
        .multilineTextAlignment(.center)
    }

    private var illustration: some View {
        Image(
            isNewUserMode
              ? ImageResource.firstProblemOnboardingNewUserIllustration
              : ImageResource.firstProblemOnboardingExistingUserIllustration
        )
        .renderingMode(.original)
        .resizable()
        .aspectRatio(contentMode: .fit)
        .frame(maxWidth: .infinity)
        .frame(maxHeight: appearance.illustrationMaxHeight)
    }

    @MainActor private var actionButton: some View {
        Button(
            buttonText,
            action: {
                actionButtonsFeedbackGenerator.triggerFeedback()
                onCallToActionButtonTap()
            }
        )
        .buttonStyle(.primary)
        .shineEffect()
    }
}

// MARK: - FirstProblemOnboardingContentView (Previews) -

#if DEBUG
#Preview("Existed User") {
    FirstProblemOnboardingContentView(
        title: "Let's keep going!",
        subtitle: "It seems you've already made progress. Continue learning on '{project(or track).title}'!",
        buttonText: "Keep learning",
        isNewUserMode: false,
        onCallToActionButtonTap: {}
    )
}

#Preview("New User") {
    FirstProblemOnboardingContentView(
        title: "Great choice!",
        subtitle: "Embark on your journey in '{project(or track).title}' right now!",
        buttonText: "Start learning",
        isNewUserMode: true,
        onCallToActionButtonTap: {}
    )
}
#endif
