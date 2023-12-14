import Foundation
import shared

final class OnboardingAssembly: Assembly {
    private weak var moduleOutput: OnboardingOutputProtocol?

    init(output: OnboardingOutputProtocol? = nil) {
        self.moduleOutput = output
    }

    func makeModule() -> OnboardingView {
        let welcomeComponent = AppGraphBridge.sharedAppGraph.buildWelcomeComponent()

        let viewModel = OnboardingViewModel(feature: welcomeComponent.welcomeFeature)
        viewModel.moduleOutput = self.moduleOutput

        return OnboardingView(viewModel: viewModel)
    }
}
