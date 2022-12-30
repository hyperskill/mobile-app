import shared
import SwiftUI

final class AuthSocialAssembly: Assembly {
    private weak var moduleOutput: AuthOutputProtocol?

    private let isInSignUpMode: Bool

    init(isInSignUpMode: Bool = false, output: AuthOutputProtocol? = nil) {
        self.isInSignUpMode = isInSignUpMode
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

        return AuthSocialView(isInSignUpMode: isInSignUpMode, viewModel: viewModel)
    }
}
