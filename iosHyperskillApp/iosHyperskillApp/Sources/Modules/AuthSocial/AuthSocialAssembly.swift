import shared
import SwiftUI

final class AuthSocialAssembly: Assembly {
    private weak var moduleOutput: AuthOutputProtocol?

    init(output: AuthOutputProtocol? = nil) {
        self.moduleOutput = output
    }

    func makeModule() -> AuthSocialView {
        let authSocialComponent = AppGraphBridge.sharedAppGraph.buildAuthSocialComponent()

        let viewModel = AuthSocialViewModel(
            socialAuthService: SocialAuthService.shared,
            authSocialErrorMapper: authSocialComponent.authSocialErrorMapper,
            feature: authSocialComponent.authSocialFeature
        )
        viewModel.moduleOutput = moduleOutput

        return AuthSocialView(viewModel: viewModel)
    }
}
