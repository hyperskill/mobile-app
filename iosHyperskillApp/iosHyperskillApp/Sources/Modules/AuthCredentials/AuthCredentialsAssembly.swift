import shared
import SwiftUI

final class AuthCredentialsAssembly: Assembly {
    private let navigationState: AppNavigationState

    init(navigationState: AppNavigationState = AppNavigationState()) {
        self.navigationState = navigationState
    }

    func makeModule() -> AuthCredentialsView {
        let feature = AppGraphBridge.shared.authComponentManual.authCredentialsFeature

        let viewModel = AuthCredentialsViewModel(
            authCredentialsErrorMapper:
                AuthCredentialsErrorMapper(resourceProvider: AppGraphBridge.shared.commonComponent.resourceProvider),
            feature: feature
        )

        return AuthCredentialsView(viewModel: viewModel, navigationState: self.navigationState)
    }
}
