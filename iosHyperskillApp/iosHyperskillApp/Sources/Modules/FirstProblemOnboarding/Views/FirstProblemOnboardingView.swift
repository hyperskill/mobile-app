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

//            FirstProblemOnboardingContentView(
//                onLearningActionButtonTap: viewModel.doLearningAction
//            )
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
}

// MARK: - FirstProblemOnboardingView (ViewAction) -

private extension FirstProblemOnboardingView {
    func handleViewAction(
        _ viewAction: FirstProblemOnboardingFeatureActionViewAction
    ) {
        switch FirstProblemOnboardingFeatureActionViewActionKs(viewAction) {
        case .completeFirstProblemOnboarding(let completeFirstProblemOnboardingViewAction):
            viewModel.doCompleteOnboarding(
                stepRoute: (completeFirstProblemOnboardingViewAction as? FirstProblemOnboardingFeatureActionViewActionCompleteFirstProblemOnboardingFirstProblemLoaded)?.stepRoute
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
            NotificationsOnboardingAssembly(output: nil).makeModule()
        }
    }
}
