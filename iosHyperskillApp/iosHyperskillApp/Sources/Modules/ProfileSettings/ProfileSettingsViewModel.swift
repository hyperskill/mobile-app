import Foundation
import shared

final class ProfileSettingsViewModel: FeatureViewModel<
  ProfileSettingsFeatureState,
  ProfileSettingsFeatureMessage,
  ProfileSettingsFeatureActionViewAction
> {
    func doLogout() {
        onNewMessage(ProfileSettingsFeatureMessageLogoutConfirmed())
    }
}
