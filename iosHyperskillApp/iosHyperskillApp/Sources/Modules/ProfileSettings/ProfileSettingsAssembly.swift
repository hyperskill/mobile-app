import SwiftUI

final class ProfileSettingsAssembly: Assembly {
    func makeModule() -> ProfileSettingsView {
        let profileSettingsComponent = AppGraphBridge.sharedAppGraph.buildProfileSettingsComponent()

        let viewModel = ProfileSettingsViewModel(feature: profileSettingsComponent.profileSettingsFeature)

        return ProfileSettingsView(viewModel: viewModel)
    }
}
