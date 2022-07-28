import shared
import SwiftUI

final class AuthCredentialsAssembly: Assembly {
    private weak var moduleOutput: AuthOutputProtocol?

    init(output: AuthOutputProtocol? = nil) {
        self.moduleOutput = output
    }

    func makeModule() -> AuthCredentialsView {
        let authCredentialsComponent = AppGraphBridge.sharedAppGraph.buildAuthCredentialsComponent()

        let viewModel = AuthCredentialsViewModel(
            authCredentialsErrorMapper: authCredentialsComponent.authCredentialsErrorMapper,
            feature: authCredentialsComponent.authCredentialsFeature
        )
        viewModel.moduleOutput = moduleOutput

        return AuthCredentialsView(viewModel: viewModel)
    }
}
