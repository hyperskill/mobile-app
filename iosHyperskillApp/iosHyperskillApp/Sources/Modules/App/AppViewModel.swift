import shared
import SwiftUI

final class AppViewModel: FeatureViewModel<AppFeatureState, AppFeatureMessage, AppFeatureActionViewAction> {
    private let analytic: Analytic

    @Published var navigationState: AppNavigationState

    init(
        analytic: Analytic,
        navigationState: AppNavigationState = AppNavigationState(),
        feature: Presentation_reduxFeature
    ) {
        self.analytic = analytic
        self.navigationState = navigationState
        super.init(feature: feature)
        self.navigationState.onSelectedTabChanged = handleSelectedTabChanged(oldValue:newValue:)
    }

    func loadApp(forceUpdate: Bool = false) {
        onNewMessage(AppFeatureMessageInit(forceUpdate: forceUpdate))
    }

    // MARK: Private API

    private func handleSelectedTabChanged(oldValue: AppTabItem, newValue: AppTabItem) {
        logClickedBottomNavigationItemEvent(oldTab: oldValue, newTab: newValue)
    }

    private func logClickedBottomNavigationItemEvent(oldTab: AppTabItem, newTab: AppTabItem) {
        func resolveAnalyticNavigationItem(
            tab: AppTabItem
        ) -> AppClickedBottomNavigationItemHyperskillAnalyticEvent.NavigationItem {
            switch tab {
            case .home:
                return .home
            case .track:
                return .track
            case .profile:
                return .profile
            }
        }

        let event = AppClickedBottomNavigationItemHyperskillAnalyticEvent(
            oldNavigationItem: resolveAnalyticNavigationItem(tab: oldTab),
            newNavigationItem: resolveAnalyticNavigationItem(tab: newTab)
        )
        analytic.reportEvent(event: event)
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
        navigationState.activeFullScreenModal = .auth
    }

    func handleOnboardingSignUpRequested() {
        navigationState.activeFullScreenModal = .newUser
    }
}

// MARK: - AppViewModel: AuthNewUserPlaceholderOutputProtocol -

extension AppViewModel: AuthNewUserPlaceholderOutputProtocol {
    func handleAuthNewUserPlaceholderSignInRequested() {
        navigationState.activeFullScreenModal = .auth
    }
}
