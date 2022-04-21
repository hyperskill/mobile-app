import shared
import SwiftUI

final class AuthEmailAssembly: Assembly {
    func makeModule() -> AuthEmailView {
        let feature = AuthCredentialsFeatureBuilder.shared.build(authInteractor: .default)
        let viewModel = AuthEmailViewModel(feature: feature)
        return AuthEmailView(viewModel: viewModel)
    }
}
