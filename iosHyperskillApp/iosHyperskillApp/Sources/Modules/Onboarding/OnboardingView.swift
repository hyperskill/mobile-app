import shared
import SwiftUI

extension OnboardingView {
    struct Appearance {
        let contentMaxWidth: CGFloat = 400

        let illustrationImageMaxHeight: CGFloat = 220
    }
}

struct OnboardingView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: OnboardingViewModel

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    private let actionButtonsFeedbackGenerator = FeedbackGenerator(feedbackType: .selection)

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView()

            buildBody()
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear {
            viewModel.stopListening()
            viewModel.onViewAction = nil
        }
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.stateKs {
        case .idle, .loading:
            ProgressView()
        case .networkError:
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: .clear,
                    action: viewModel.doRetryLoadOnboarding
                )
            )
        case .content:
            VStack(alignment: .center, spacing: LayoutInsets.largeInset) {
                if horizontalSizeClass == .regular {
                    Spacer()
                }

                Text(Strings.Onboarding.title)
                    .font(.largeTitle).bold()
                    .foregroundColor(.newPrimaryText)
                    .multilineTextAlignment(.center)

                Text(Strings.Onboarding.text)
                    .font(.title3)
                    .foregroundColor(.newPrimaryText)
                    .multilineTextAlignment(.center)

                Spacer()

                Image(.onboardingIllustration)
                    .renderingMode(.original)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(maxWidth: .infinity, maxHeight: appearance.illustrationImageMaxHeight)

                Spacer()

                Button(
                    Strings.Onboarding.primaryButton,
                    action: {
                        actionButtonsFeedbackGenerator.triggerFeedback()
                        viewModel.doPrimaryButtonAction()
                    }
                )
                .buttonStyle(.primary)
                .shineEffect()

                Button(
                    Strings.Onboarding.secondaryButton,
                    action: {
                        actionButtonsFeedbackGenerator.triggerFeedback()
                        viewModel.doSecondaryButtonAction()
                    }
                )
                .buttonStyle(.tertiary)

                if horizontalSizeClass == .regular {
                    Spacer()
                }
            }
            .frame(maxWidth: appearance.contentMaxWidth)
            .padding()
        }
    }

    private func handleViewAction(_ viewAction: OnboardingFeatureActionViewAction) {
        switch OnboardingFeatureActionViewActionKs(viewAction) {
        case .navigateTo(let navigateToViewAction):
            switch OnboardingFeatureActionViewActionNavigateToKs(navigateToViewAction) {
            case .authScreen(let data):
                viewModel.doSignUpPresentation(isInSignUpMode: data.isInSignUpMode)
            case .trackSelectionListScreen:
                viewModel.doSignPresentation()
            }
        }
    }
}

struct OnboardingView_Previews: PreviewProvider {
    static var previews: some View {
        OnboardingAssembly()
            .makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))

        OnboardingAssembly()
            .makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))
            .preferredColorScheme(.dark)

        OnboardingAssembly()
            .makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
    }
}
