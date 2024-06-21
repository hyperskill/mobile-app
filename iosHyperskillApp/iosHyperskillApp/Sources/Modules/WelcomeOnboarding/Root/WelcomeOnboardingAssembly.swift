import shared
import UIKit

final class WelcomeOnboardingAssembly: UIKitAssembly {
    private let params: WelcomeOnboardingFeatureParams

    private weak var moduleOutput: WelcomeOnboardingOutputProtocol?

    init(params: WelcomeOnboardingFeatureParams, moduleOutput: WelcomeOnboardingOutputProtocol?) {
        self.params = params
        self.moduleOutput = moduleOutput
    }

    func makeModule() -> UIViewController {
        let welcomeOnboardingComponent = AppGraphBridge.sharedAppGraph.buildWelcomeOnboardingComponent(
            params: params
        )

        let welcomeOnboardingViewModel = WelcomeOnboardingViewModel(
            feature: welcomeOnboardingComponent.welcomeOnboardingFeature
        )
        welcomeOnboardingViewModel.moduleOutput = moduleOutput

        let welcomeOnboardingViewController = WelcomeOnboardingViewController(
            viewModel: welcomeOnboardingViewModel
        )
        welcomeOnboardingViewModel.viewController = welcomeOnboardingViewController

        return welcomeOnboardingViewController
    }
}
