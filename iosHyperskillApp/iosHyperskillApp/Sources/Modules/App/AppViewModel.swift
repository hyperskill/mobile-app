import Combine
import Foundation
import shared
import SwiftUI

final class AppViewModel: FeatureViewModel<AppFeatureState, AppFeatureMessage, AppFeatureActionViewAction> {
    weak var viewController: AppViewControllerProtocol?

    private var pushNotificationData: PushNotificationData?

    private let analytic: Analytic

    private var objectWillChangeSubscription: AnyCancellable?

    init(
        pushNotificationData: PushNotificationData?,
        analytic: Analytic,
        feature: Presentation_reduxFeature
    ) {
        self.pushNotificationData = pushNotificationData
        self.analytic = analytic

        super.init(feature: feature)

        self.objectWillChangeSubscription = objectWillChange.sink { [weak self] _ in
            self?.mainScheduler.schedule { [weak self] in
                guard let strongSelf = self else {
                    return
                }

                let stateKs = AppFeatureStateKs(strongSelf.state)

                if case .ready = stateKs {
                    strongSelf.pushNotificationData = nil
                }

                strongSelf.viewController?.displayState(stateKs)
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

        subscribeForNotifications()
    }

    override func shouldNotifyStateDidChange(oldState: AppFeatureState, newState: AppFeatureState) -> Bool {
        AppFeatureStateKs(oldState) != AppFeatureStateKs(newState)
    }

    func doLoadApp(forceUpdate: Bool = false) {
        onNewMessage(AppFeatureMessageInitialize(pushNotificationData: pushNotificationData, forceUpdate: forceUpdate))
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
            case .studyPlan:
                return .studyPlan
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
        onNewMessage(
            AppFeatureMessageUserAuthorized(
                profile: profile,
                isNotificationPermissionGranted: false
            )
        )
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
            onViewAction?(AppFeatureActionViewActionNavigateToTrackSelectionScreen())
        }
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

// MARK: - AppViewModel (NotificationCenter) -

private extension AppViewModel {
    func subscribeForNotifications() {
        let notificationCenter = NotificationCenter.default

        notificationCenter.addObserver(
            self,
            selector: #selector(handleTrackSelectionDetailsDidRequestNavigateToHomeAsNewRootScreen),
            name: .trackSelectionDetailsDidRequestNavigateToHomeAsNewRootScreen,
            object: nil
        )
        notificationCenter.addObserver(
            self,
            selector: #selector(handleProjectSelectionDetailsDidRequestNavigateToHomeAsNewRootScreen),
            name: .projectSelectionDetailsDidRequestNavigateToHomeAsNewRootScreen,
            object: nil
        )

        notificationCenter.addObserver(
            self,
            selector: #selector(handleRemoteNotificationClicked(notification:)),
            name: .remoteNotificationClicked,
            object: nil
        )

        notificationCenter.addObserver(
            self,
            selector: #selector(handleDeviceOrientationDidChange),
            name: UIDevice.orientationDidChangeNotification,
            object: nil
        )
    }

    @objc
    func handleTrackSelectionDetailsDidRequestNavigateToHomeAsNewRootScreen() {
        onViewAction?(AppFeatureActionViewActionNavigateToHomeScreen())
    }

    @objc
    func handleProjectSelectionDetailsDidRequestNavigateToHomeAsNewRootScreen() {
        onViewAction?(AppFeatureActionViewActionNavigateToHomeScreen())
    }

    @objc
    func handleRemoteNotificationClicked(notification: Foundation.Notification) {
        let key = NotificationsService.PayloadKey.pushNotificationData.rawValue

        guard let pushNotificationData = notification.userInfo?[key] as? PushNotificationData else {
            #if DEBUG
            print(
"""
AppViewModel: \(#function) PushNotificationData not found in userInfo = \(String(describing: notification.userInfo))
"""
            )
            #endif
            return
        }

        onNewMessage(AppFeatureMessageNotificationClicked(notificationData: pushNotificationData))
    }

    @objc
    private func handleDeviceOrientationDidChange() {
        let screenOrientation: ScreenOrientation = DeviceInfo.current.orientation.isLandscape ? .landscape : .portrait

        #if DEBUG
        print("AppViewModel: screenOrientation = \(screenOrientation)")
        #endif

        analytic.setScreenOrientation(screenOrientation: screenOrientation)
    }
}

// MARK: - AppViewModel: StreakRecoveryModalDelegate -

extension AppViewModel: StreakRecoveryModalViewControllerDelegate {
    func streakRecoveryModalViewControllerDidTapRestoreStreakButton() {
        onNewMessage(
            AppFeatureMessageStreakRecoveryMessage(
                message: StreakRecoveryFeatureMessageRestoreStreakClicked()
            )
        )
    }

    func streakRecoveryModalViewControllerDidTapNoThanksButton() {
        onNewMessage(
            AppFeatureMessageStreakRecoveryMessage(
                message: StreakRecoveryFeatureMessageNoThanksClicked()
            )
        )
    }

    func streakRecoveryModalViewControllerDidAppear(_ viewController: StreakRecoveryModalViewController) {
        onNewMessage(
            AppFeatureMessageStreakRecoveryMessage(
                message: StreakRecoveryFeatureMessageStreakRecoveryModalShownEventMessage()
            )
        )
    }

    func streakRecoveryModalViewControllerDidDisappear(_ viewController: StreakRecoveryModalViewController) {
        onNewMessage(
            AppFeatureMessageStreakRecoveryMessage(
                message: StreakRecoveryFeatureMessageStreakRecoveryModalHiddenEventMessage()
            )
        )
    }
}

// MARK: - AppViewModel: BadgeEarnedModalViewControllerDelegate -

extension AppViewModel: BadgeEarnedModalViewControllerDelegate {
    func badgeEarnedModalViewControllerDidAppear(
        _ viewController: BadgeEarnedModalViewController, badgeKind: BadgeKind
    ) {
        onNewMessage(
            AppFeatureMessageNotificationClickHandlingMessage(
                message: NotificationClickHandlingFeatureMessageEarnedBadgeModalShownEventMessage(badgeKind: badgeKind)
            )
        )
    }

    func badgeEarnedModalViewControllerDidDisappear(
        _ viewController: BadgeEarnedModalViewController, badgeKind: BadgeKind
    ) {
        onNewMessage(
            AppFeatureMessageNotificationClickHandlingMessage(
                message: NotificationClickHandlingFeatureMessageEarnedBadgeModalHiddenEventMessage(badgeKind: badgeKind)
            )
        )
    }
}
