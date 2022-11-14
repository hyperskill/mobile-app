import Combine
import shared
import SwiftUI

final class AppViewModel: FeatureViewModel<AppFeatureState, AppFeatureMessage, AppFeatureActionViewAction> {
    weak var viewController: AppViewControllerProtocol?

    private let analytic: Analytic

    private var objectWillChangeSubscription: AnyCancellable?

    init(analytic: Analytic, feature: Presentation_reduxFeature) {
        self.analytic = analytic

        super.init(feature: feature)

        self.objectWillChangeSubscription = objectWillChange.sink { [weak self] _ in
            self?.mainScheduler.schedule { [weak self] in
                guard let strongSelf = self else {
                    return
                }

                strongSelf.viewController?.displayState(strongSelf.state)
            }
        }
        self.onViewAction = { [weak self] viewAction in
            self?.mainScheduler.schedule { [weak self] in
                guard let strongSelf = self else {
                    return
                }

                strongSelf.viewController?.displayViewAction(viewAction)
            }
        }
    }

    func loadApp(forceUpdate: Bool = false) {
        onNewMessage(AppFeatureMessageInitialize(forceUpdate: forceUpdate))
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
        onNewMessage(AppFeatureMessageUserAuthorized(isNewUser: isNewUser))
    }
}

// MARK: - AppViewModel: OnboardingOutputProtocol -

extension AppViewModel: OnboardingOutputProtocol {
    func handleOnboardingSignInRequested() {
        onViewAction?(AppFeatureActionViewActionNavigateToAuthScreen())
    }

    func handleOnboardingSignUpRequested() {
        onViewAction?(AppFeatureActionViewActionNavigateToNewUserScreen())
    }
}

// MARK: - AppViewModel: AuthNewUserPlaceholderOutputProtocol -

extension AppViewModel: AuthNewUserPlaceholderOutputProtocol {
    func handleAuthNewUserPlaceholderSignInRequested() {
        onViewAction?(AppFeatureActionViewActionNavigateToAuthScreen())
    }
}

// MARK: - AppViewModel: AppTabBarControllerDelegate -

extension AppViewModel: AppTabBarControllerDelegate {
    func appTabBarController(
        _ controller: AppTabBarController,
        didSelectTabItem newTabItem: AppTabItem,
        oldTabItem: AppTabItem
    ) {
        handleSelectedTabChanged(oldValue: oldTabItem, newValue: newTabItem)
    }
}
