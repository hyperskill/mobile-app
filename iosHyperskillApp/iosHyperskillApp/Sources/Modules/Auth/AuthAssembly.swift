import shared
import SwiftUI

final class AuthAssembly: Assembly {
    func makeModule() -> AuthView {
        let authFeature = AuthFeatureBuilder.shared.build()

        let authViewModel = AuthViewModel(feature: authFeature)

        return AuthView(viewModel: authViewModel)
    }
}
