import Foundation
import shared

final class OnboardingAssembly: Assembly {
    private weak var moduleOutput: OnboardingOutputProtocol?

    init(output: OnboardingOutputProtocol? = nil) {
        self.moduleOutput = output
    }

    func makeModule() -> OnboardingView {
        let onboardingComponent = AppGraphBridge.sharedAppGraph.buildOnboardingComponent()

        let viewModel = OnboardingViewModel(feature: onboardingComponent.onboardingFeature)

        viewModel.moduleOutput = self.moduleOutput

        return OnboardingView(viewModel: viewModel)
    }
}
