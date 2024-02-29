import AppTrackingTransparency
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
            // swiftlint:disable switch_case_on_newline
            switch tab {
            case .home: .home
            case .studyPlan: .studyPlan
            case .leaderboard: .leaderboard
            case .profile: .profile
            case .debug: .debug
            }
            // swiftlint:enable switch_case_on_newline
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
        Task(priority: .userInitiated) {
            let currentAuthorizationStatus = await NotificationPermissionStatus.current

            await MainActor.run {
                onNewMessage(
                    AppFeatureMessageUserAuthorized(
                        profile: profile,
                        isNotificationPermissionGranted: currentAuthorizationStatus.isRegistered
                    )
                )
            }
        }
    }
}

// MARK: - AppViewModel: WelcomeOutputProtocol -

extension AppViewModel: WelcomeOutputProtocol {
    func handleWelcomeSignInRequested() {
        onViewAction?(AppFeatureActionViewActionNavigateToAuthScreen(isInSignUpMode: false))
    }

    func handleWelcomeSignUpRequested(isInSignUpMode: Bool) {
        if isInSignUpMode {
            onViewAction?(AppFeatureActionViewActionNavigateToAuthScreen(isInSignUpMode: isInSignUpMode))
        } else {
            onViewAction?(AppFeatureActionViewActionNavigateToTrackSelectionScreen())
        }
    }
}

// MARK: - AppViewModel: NotificationsOnboardingOutputProtocol -

extension AppViewModel: NotificationsOnboardingOutputProtocol {
    func handleNotificationsOnboardingCompleted() {
        onNewMessage(
            AppFeatureMessageWelcomeOnboardingMessage(
                message: WelcomeOnboardingFeatureMessageNotificationOnboardingCompleted()
            )
        )
    }
}

// MARK: - AppViewModel: UsersQuestionnaireOnboardingOutputProtocol -

extension AppViewModel: UsersQuestionnaireOnboardingOutputProtocol {
    func handleUsersQuestionnaireOnboardingCompleted() {
        onNewMessage(
            AppFeatureMessageWelcomeOnboardingMessage(
                message: WelcomeOnboardingFeatureMessageUsersQuestionnaireOnboardingCompleted()
            )
        )
    }
}

// MARK: - AppViewModel: FirstProblemOnboardingOutputProtocol -

extension AppViewModel: FirstProblemOnboardingOutputProtocol {
    func handleFirstProblemOnboardingCompleted(stepRoute: StepRoute?) {
        onNewMessage(
            AppFeatureMessageWelcomeOnboardingMessage(
                message: WelcomeOnboardingFeatureMessageFirstProblemOnboardingCompleted(
                    firstProblemStepRoute: stepRoute
                )
            )
        )
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
            selector: #selector(handleTrackSelectionDetailsDidRequestNavigateToFirstProblemOnboarding),
            name: .trackSelectionDetailsDidRequestNavigateToFirstProblemOnboarding,
            object: nil
        )

        notificationCenter.addObserver(
            self,
            selector: #selector(handleProjectSelectionDetailsDidRequestNavigateToStudyPlanAsNewRootScreen),
            name: .projectSelectionDetailsDidRequestNavigateToStudyPlanAsNewRootScreen,
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
        notificationCenter.addObserver(
            self,
            selector: #selector(handleAppTrackingTransparencyAuthorizationStatusDidChange(notification:)),
            name: .attAuthorizationStatusDidChange,
            object: nil
        )

        notificationCenter.addObserver(
            self,
            selector: #selector(handleApplicationWillEnterForeground),
            name: UIApplication.willEnterForegroundNotification,
            object: UIApplication.shared
        )
    }

    @objc
    func handleProjectSelectionDetailsDidRequestNavigateToStudyPlanAsNewRootScreen() {
        onViewAction?(AppFeatureActionViewActionNavigateToStudyPlan())
    }

    @objc
    func handleTrackSelectionDetailsDidRequestNavigateToFirstProblemOnboarding() {
        onViewAction?(
            AppFeatureActionViewActionWelcomeOnboardingViewAction(
                viewAction: WelcomeOnboardingFeatureActionViewActionNavigateToFirstProblemOnboardingScreen(
                    isNewUserMode: true
                )
            )
        )
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

    @objc
    private func handleAppTrackingTransparencyAuthorizationStatusDidChange(notification: Foundation.Notification) {
        guard let authorizationStatus = notification.object as? ATTrackingManager.AuthorizationStatus else {
            return assertionFailure("ATTrackingManager.AuthorizationStatus")
        }

        #if DEBUG
        print("AppViewModel: attAuthorizationStatus = \(authorizationStatus)")
        #endif

        let isAuthorized = authorizationStatus == .authorized
        analytic.setAppTrackingTransparencyAuthorizationStatus(isAuthorized: isAuthorized)
    }

    @objc
    private func handleApplicationWillEnterForeground() {
        #warning("Enable when subscription purchase is implemented")
        //onNewMessage(AppFeatureMessageAppBecomesActive())
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
