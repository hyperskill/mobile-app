import shared
import SwiftUI

final class AuthNewUserPlaceholderAssembly: Assembly {
    func makeModule() -> AuthNewUserPlaceholderView {
        let placeholderNewUserComponent = AppGraphBridge.sharedAppGraph.buildPlaceholderNewUserComponent()

        let viewModel = AuthNewUserPlaceholderViewModel(
            feature: placeholderNewUserComponent.placeholderNewUserFeature
        )

        return AuthNewUserPlaceholderView(viewModel: viewModel)
    }
}
