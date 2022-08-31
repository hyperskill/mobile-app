import shared
import SwiftUI

final class AppViewModel: FeatureViewModel<AppFeatureState, AppFeatureMessage, AppFeatureActionViewAction> {
    @Published var navigationState = AppNavigationState()

    func loadApp(forceUpdate: Bool = false) {
        onNewMessage(AppFeatureMessageInit(forceUpdate: forceUpdate))
    }
}

// MARK: - AppViewModel: AuthOutputProtocol -

extension AppViewModel: AuthOutputProtocol {
    func handleUserAuthorized(isNewUser: Bool) {
        navigationState.activeFullScreenModal = nil
        onNewMessage(AppFeatureMessageUserAuthorized(isNewUser: isNewUser))
    }
}

// MARK: - AppViewModel: OnboardingOutputProtocol -

extension AppViewModel: OnboardingOutputProtocol {
    func handleOnboardingSignInRequested() {
        onNewMessage(AppFeatureMessageOpenAuthScreen())
    }

    func handleOnboardingSignUpRequested() {
        onNewMessage(AppFeatureMessageOpenNewUserScreen())
    }
}
