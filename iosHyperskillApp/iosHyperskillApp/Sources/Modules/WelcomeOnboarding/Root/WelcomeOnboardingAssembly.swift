import shared
import UIKit

final class WelcomeOnboardingAssembly: UIKitAssembly {
    private let params: WelcomeOnboardingFeatureParams

    init(params: WelcomeOnboardingFeatureParams) {
        self.params = params
    }

    func makeModule() -> UIViewController {
        let welcomeOnboardingComponent = AppGraphBridge.sharedAppGraph.buildWelcomeOnboardingComponent(
            params: params
        )

        let welcomeOnboardingViewModel = WelcomeOnboardingViewModel(
            feature: welcomeOnboardingComponent.welcomeOnboardingFeature
        )

        let welcomeOnboardingViewController = WelcomeOnboardingViewController(
            viewModel: welcomeOnboardingViewModel
        )
        welcomeOnboardingViewModel.viewController = welcomeOnboardingViewController

        return welcomeOnboardingViewController
    }
}
