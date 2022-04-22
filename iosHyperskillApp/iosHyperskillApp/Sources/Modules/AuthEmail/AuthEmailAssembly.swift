import shared
import SwiftUI

final class AuthEmailAssembly: Assembly {
    private let navigationState: AppNavigationState

    init(navigationState: AppNavigationState = AppNavigationState()) {
        self.navigationState = navigationState
    }

    func makeModule() -> AuthEmailView {
        let feature = AuthCredentialsFeatureBuilder.shared.build(authInteractor: .default)
        let viewModel = AuthEmailViewModel(feature: feature)
        return AuthEmailView(viewModel: viewModel, navigationState: self.navigationState)
    }
}
