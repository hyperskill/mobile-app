import Foundation
import shared

final class SettingsViewModel: FeatureViewModel<
ProfileSettingsFeatureState,
ProfileSettingsFeatureMessage,
ProfileSettingsFeatureActionViewAction
> {
    func logout() {
        onNewMessage(ProfileSettingsFeatureMessageLoggedOut())
    }
}
