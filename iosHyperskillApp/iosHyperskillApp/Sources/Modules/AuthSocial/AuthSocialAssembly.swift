import shared
import SwiftUI

final class AuthSocialAssembly: Assembly {
    private let navigationState: AppNavigationState

    init(navigationState: AppNavigationState = AppNavigationState()) {
        self.navigationState = navigationState
    }

    func makeModule() -> AuthSocialView {
        let authSocialComponent = AppGraphBridge.shared.buildAuthSocialComponent()

        let viewModel = AuthSocialViewModel(
            socialAuthService: SocialAuthService.shared,
            authSocialErrorMapper: authSocialComponent.authSocialErrorMapper,
            feature: authSocialComponent.authSocialFeature
        )

        return AuthSocialView(viewModel: viewModel, navigationState: self.navigationState)
    }
}
