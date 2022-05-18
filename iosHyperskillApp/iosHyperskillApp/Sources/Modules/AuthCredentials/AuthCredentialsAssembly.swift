import shared
import SwiftUI

final class AuthCredentialsAssembly: Assembly {
    private let navigationState: AppNavigationState

    init(navigationState: AppNavigationState = AppNavigationState()) {
        self.navigationState = navigationState
    }

    func makeModule() -> AuthCredentialsView {
        let feature = AuthCredentialsFeatureBuilder.shared.build(authInteractor: .default)

        let viewModel = AuthCredentialsViewModel(
            authCredentialsErrorMapper: AuthCredentialsErrorMapper(resourceProvider: ResourceProviderImpl()),
            feature: feature
        )

        return AuthCredentialsView(viewModel: viewModel, navigationState: self.navigationState)
    }
}
