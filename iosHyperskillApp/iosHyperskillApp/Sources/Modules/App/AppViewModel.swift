import shared
import SwiftUI

final class AppViewModel: FeatureViewModel<AppFeatureState, AppFeatureMessage, AppFeatureActionViewAction> {
    @Published var navigationState = AppNavigationState()

    func loadApp(forceUpdate: Bool = false) {
        onNewMessage(AppFeatureMessageInit(forceUpdate: forceUpdate))
    }

    func doAuthPresentation() {
        onNewMessage(AppFeatureMessageOpenAuthScreen())
    }

    func doNewUserPresentation() {
        onNewMessage(AppFeatureMessageOpenNewUserScreen())
    }
}

// MARK: - AppViewModel: AuthOutputProtocol -

extension AppViewModel: AuthOutputProtocol {
    func handleUserAuthorized(isNewUser: Bool) {
        navigationState.activeFullScreenModal = nil
        onNewMessage(AppFeatureMessageUserAuthorized(isNewUser: isNewUser))
        NotificationsRegistrationService.requestAuthorization()
    }
}
