import shared
import SwiftUI

extension WelcomeView {
    struct Appearance {
        let contentMaxWidth: CGFloat = 400

        let illustrationImageMaxHeight: CGFloat = 220
    }
}

struct WelcomeView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: WelcomeViewModel

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    private let actionButtonsFeedbackGenerator = FeedbackGenerator(feedbackType: .selection)

    private var isDebugButtonVisible: Bool {
        #if DEBUG
        return true
        #else
        return ApplicationInfo.isDebugModeAvailable
        #endif
    }

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
                    action: viewModel.doRetryContentLoading
                )
            )
        case .content:
            VStack(alignment: .center, spacing: LayoutInsets.largeInset) {
                if horizontalSizeClass == .regular {
                    Spacer()
                }

                Text(Strings.Welcome.title)
                    .font(.largeTitle).bold()
                    .foregroundColor(.newPrimaryText)
                    .multilineTextAlignment(.center)

                Text(Strings.Welcome.text)
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
                    Strings.Welcome.primaryButton,
                    action: {
                        actionButtonsFeedbackGenerator.triggerFeedback()
                        viewModel.doPrimaryButtonAction()
                    }
                )
                .buttonStyle(.primary)
                .shineEffect()

                Button(
                    Strings.Welcome.secondaryButton,
                    action: {
                        actionButtonsFeedbackGenerator.triggerFeedback()
                        viewModel.doSecondaryButtonAction()
                    }
                )

                if horizontalSizeClass == .regular {
                    Spacer()
                }

                if isDebugButtonVisible {
                    Button("Display Debug Screen") {
                        SourcelessRouter().currentPresentedViewController()?.present(
                            DebugAssembly().makeModule(),
                            animated: true
                        )
                    }
                }
            }
            .frame(maxWidth: appearance.contentMaxWidth)
            .padding()
        }
    }

    private func handleViewAction(_ viewAction: WelcomeFeatureActionViewAction) {
        switch WelcomeFeatureActionViewActionKs(viewAction) {
        case .navigateTo(let navigateToViewAction):
            switch WelcomeFeatureActionViewActionNavigateToKs(navigateToViewAction) {
            case .authScreen(let data):
                viewModel.doSignUpPresentation(isInSignUpMode: data.isInSignUpMode)
            case .trackSelectionListScreen:
                viewModel.doSignInPresentation()
            }
        }
    }
}

struct OnboardingView_Previews: PreviewProvider {
    static var previews: some View {
        WelcomeAssembly()
            .makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))

        WelcomeAssembly()
            .makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))
            .preferredColorScheme(.dark)

        WelcomeAssembly()
            .makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
    }
}
