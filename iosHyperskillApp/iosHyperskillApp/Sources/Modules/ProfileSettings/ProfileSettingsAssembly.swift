import SwiftUI

final class ProfileSettingsAssembly: Assembly {
    func makeModule() -> ProfileSettingsView {
        let profileSettingsComponent = AppGraphBridge.sharedAppGraph.buildProfileSettingsComponent()

        let applicationThemeService = ApplicationThemeService(
            profileSettingsInteractor: profileSettingsComponent.profileSettingsInteractor
        )
        let viewModel = ProfileSettingsViewModel(
            applicationThemeService: applicationThemeService,
            feature: profileSettingsComponent.profileSettingsFeature
        )

        return ProfileSettingsView(viewModel: viewModel)
    }
}
