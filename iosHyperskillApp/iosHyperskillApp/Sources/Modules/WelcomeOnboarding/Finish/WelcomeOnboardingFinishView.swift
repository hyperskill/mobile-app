import shared
import SwiftUI

extension WelcomeOnboardingFinishView {
    struct Appearance {
        let interitemSpacing = LayoutInsets.smallInset

        let illustrationMaxHeight: CGFloat = 380

        let maxWidth: CGFloat = DeviceInfo.current.isPad ? 400 : .infinity
    }
}

struct WelcomeOnboardingFinishView: View {
    private(set) var appearance = Appearance()

    let viewState: WelcomeOnboardingFinishViewState

    let onViewDidAppear: () -> Void
    let onCallToActionButtonTap: () -> Void

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: onViewDidAppear)

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
    }

    private var header: some View {
        VStack(alignment: .center, spacing: appearance.interitemSpacing) {
            Text(viewState.title)
                .font(.title).bold()
                .foregroundColor(.newPrimaryText)

            Text(viewState.description_)
                .font(.body)
                .foregroundColor(.newSecondaryText)
        }
        .multilineTextAlignment(.center)
    }

    private var illustration: some View {
        Image(.firstProblemOnboardingExistingUserIllustration)
            .renderingMode(.original)
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(maxWidth: .infinity, maxHeight: appearance.illustrationMaxHeight)
    }

    @MainActor private var actionButton: some View {
        Button(
            viewState.buttonText,
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
    WelcomeOnboardingFinishView(
        viewState: WelcomeOnboardingFinishViewState(
            title: "You're all set!",
            description: "Embark on your journey in Java right now!",
            buttonText: "Start my journey"
        ),
        onViewDidAppear: {},
        onCallToActionButtonTap: {}
    )
}
#endif
