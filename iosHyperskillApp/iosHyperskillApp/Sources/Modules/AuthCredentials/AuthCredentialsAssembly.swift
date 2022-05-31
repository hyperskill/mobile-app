import shared
import SwiftUI

final class AuthCredentialsAssembly: Assembly {
    private let navigationState: AppNavigationState

    init(navigationState: AppNavigationState = AppNavigationState()) {
        self.navigationState = navigationState
    }

    func makeModule() -> AuthCredentialsView {
        let authCredentialsComponent = AppGraphBridge.shared.buildAuthCredentialsComponent()

        let viewModel = AuthCredentialsViewModel(
            authCredentialsErrorMapper: authCredentialsComponent.authCredentialsErrorMapper,
            feature: authCredentialsComponent.authCredentialsFeature
        )

        return AuthCredentialsView(viewModel: viewModel, navigationState: self.navigationState)
    }
}
