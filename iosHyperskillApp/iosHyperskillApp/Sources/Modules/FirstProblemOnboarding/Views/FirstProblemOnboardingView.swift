import shared
import SwiftUI

extension FirstProblemOnboardingView {
    struct Appearance {
        let backgroundColor = Color(ColorPalette.newLayer1)
    }
}

struct FirstProblemOnboardingView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: FirstProblemOnboardingViewModel

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: appearance.backgroundColor)

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
        case .error:
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: .clear,
                    action: viewModel.doRetryContentLoading
                )
            )
        case .content(let content):
            let _ = handleLearningActivityLoadingIndicatorVisibility(
                isVisible: content.isLearningActivityLoading
            )

            FirstProblemOnboardingContentView(
                title: content.title,
                subtitle: content.subtitle,
                buttonText: content.buttonText,
                isNewUserMode: content.isNewUserMode,
                onCallToActionButtonTap: viewModel.doCallToAction
            )
        }
    }

    private func handleLearningActivityLoadingIndicatorVisibility(isVisible: Bool) {
        if isVisible {
            ProgressHUD.show()
        } else {
            ProgressHUD.dismissWithDelay()
        }
    }
}

// MARK: - FirstProblemOnboardingView (ViewAction) -

private extension FirstProblemOnboardingView {
    func handleViewAction(
        _ viewAction: FirstProblemOnboardingFeatureActionViewAction
    ) {
        switch FirstProblemOnboardingFeatureActionViewActionKs(viewAction) {
        case .completeFirstProblemOnboarding(let completeFirstProblemOnboardingViewAction):
            viewModel.doCompleteOnboarding(
                stepRoute: completeFirstProblemOnboardingViewAction.stepRoute
            )
        case .showNetworkError:
            ProgressHUD.showError(status: Strings.FirstProblemOnboarding.networkError)
        }
    }
}

// MARK: - FirstProblemOnboardingView_Previews: PreviewProvider -

struct FirstProblemOnboardingView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            FirstProblemOnboardingAssembly(isNewUserMode: true, output: nil).makeModule()
        }

        UIKitViewControllerPreview {
            FirstProblemOnboardingAssembly(isNewUserMode: false, output: nil).makeModule()
        }
    }
}
