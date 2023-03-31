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

                strongSelf.viewController?.displayState(AppFeatureStateKs(strongSelf.state))
            }
        }
        self.onViewAction = { [weak self] viewAction in
            self?.mainScheduler.schedule { [weak self] in
                guard let strongSelf = self else {
                    return
                }

                strongSelf.viewController?.displayViewAction(AppFeatureActionViewActionKs(viewAction))
            }
        }
    }

    override func shouldNotifyStateDidChange(oldState: AppFeatureState, newState: AppFeatureState) -> Bool {
        AppFeatureStateKs(oldState) != AppFeatureStateKs(newState)
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
            case .debug:
                return .debug
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
    func handleUserAuthorized(profile: Profile) {
        onNewMessage(AppFeatureMessageUserAuthorized(profile: profile))
    }
}

// MARK: - AppViewModel: OnboardingOutputProtocol -

extension AppViewModel: OnboardingOutputProtocol {
    func handleOnboardingSignInRequested() {
        onViewAction?(AppFeatureActionViewActionNavigateToAuthScreen(isInSignUpMode: false))
    }

    func handleOnboardingSignUpRequested(isInSignUpMode: Bool) {
        if isInSignUpMode {
            onViewAction?(AppFeatureActionViewActionNavigateToAuthScreen(isInSignUpMode: isInSignUpMode))
        } else {
            onViewAction?(AppFeatureActionViewActionNavigateToNewUserScreen())
        }
    }
}

// MARK: - AppViewModel: AuthNewUserPlaceholderOutputProtocol -

extension AppViewModel: AuthNewUserPlaceholderOutputProtocol {
    func handleAuthNewUserPlaceholderDidRequestNavigateToHome() {
        onViewAction?(AppFeatureActionViewActionNavigateToHomeScreen())
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
