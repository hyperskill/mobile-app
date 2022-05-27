import shared
import SwiftUI

final class AuthSocialAssembly: Assembly {
    private let navigationState: AppNavigationState

    init(navigationState: AppNavigationState = AppNavigationState()) {
        self.navigationState = navigationState
    }

    func makeModule() -> AuthSocialView {
        let feature = AppGraphBridge.shared.authComponentManual.authSocialFeature

        let viewModel = AuthSocialViewModel(
            socialAuthService: SocialAuthService.shared,
            authSocialErrorMapper: AuthSocialErrorMapper(resourceProvider: AppGraphBridge.shared.commonComponent.resourceProvider),
            feature: feature
        )

        return AuthSocialView(viewModel: viewModel, navigationState: self.navigationState)
    }
}
