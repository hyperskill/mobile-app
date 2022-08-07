import SwiftUI

final class SettingsAssembly: Assembly {
    func makeModule() -> SettingsView {
        let settingsComponent = AppGraphBridge.sharedAppGraph.buildProfileSettingsComponent()

        let viewModel = SettingsViewModel(
            feature: settingsComponent.profileSettingsFeature
        )

        return SettingsView(viewModel: viewModel)
    }
}
