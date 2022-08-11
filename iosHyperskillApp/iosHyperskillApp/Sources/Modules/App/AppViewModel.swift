import shared
import SwiftUI

final class AppViewModel: FeatureViewModel<AppFeatureState, AppFeatureMessage, AppFeatureActionViewAction> {
    @Published var navigationState = AppNavigationState()

    func loadApp(forceUpdate: Bool = false) {
        onNewMessage(AppFeatureMessageInit(forceUpdate: forceUpdate))
    }

    func openAuthScreen() {
        onNewMessage(AppFeatureMessageOpenAuthScreen())
    }

    func openNewUserScreen() {
        onNewMessage(AppFeatureMessageOpenNewUserScreen())
    }
}

// MARK: - AppViewModel: AuthOutputProtocol -

extension AppViewModel: AuthOutputProtocol {
    func handleUserAuthorized(isNewUser: Bool) {
        navigationState.presentingScreen = nil
        onNewMessage(AppFeatureMessageUserAuthorized(isNewUser: isNewUser))
        NotificationsRegistrationService.requestAuthorization()
    }
}
