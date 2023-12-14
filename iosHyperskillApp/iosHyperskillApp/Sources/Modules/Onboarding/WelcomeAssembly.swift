import Foundation
import shared

final class WelcomeAssembly: Assembly {
    private weak var moduleOutput: WelcomeOutputProtocol?

    init(output: WelcomeOutputProtocol? = nil) {
        self.moduleOutput = output
    }

    func makeModule() -> WelcomeView {
        let welcomeComponent = AppGraphBridge.sharedAppGraph.buildWelcomeComponent()

        let viewModel = WelcomeViewModel(feature: welcomeComponent.welcomeFeature)
        viewModel.moduleOutput = self.moduleOutput

        return WelcomeView(viewModel: viewModel)
    }
}
