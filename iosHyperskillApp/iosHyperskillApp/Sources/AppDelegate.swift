import FirebaseCore
import GoogleSignIn
import SwiftUI
import UIKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?

    // swiftlint:disable:next weak_delegate
    private lazy var userNotificationsCenterDelegate = UserNotificationsCenterDelegate()
    private lazy var notificationsService = NotificationsService()
    private lazy var notificationPermissionStatusSettingsObserver = NotificationPermissionStatusSettingsObserver.default
    private lazy var notificationsRegistrationService = NotificationsRegistrationService.shared

    private lazy var interfaceOrientationManager = InterfaceOrientationManager()

    // MARK: Initializing the App

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        window = UIWindow(frame: UIScreen.main.bounds)

        let launchOptionsResult = notificationsService.handleLaunchOptions(launchOptions)

        let rootViewController = AppAssembly(
            pushNotificationData: launchOptionsResult.pushNotificationData
        ).makeModule()
        window?.rootViewController = rootViewController

        // Sentry SDK observing UIWindowDidBecomeVisibleNotification for correct working of the SentryAppStartTracker,
        // so it's necessary to setup SDK before showing the window
        SentryManager.shared.setup()

        window?.makeKeyAndVisible()

        AppAppearance.themeApplication(window: window.require())
        ApplicationThemeService.default.applyDefaultTheme()

        FirebaseApp.configure()
        ProgressHUD.configure()
        KeyboardManager.configure()
        NukeManager.registerCustomDecoders()
        AppPowerModeObserver.shared.observe()

        notificationsRegistrationService.renewAPNsDeviceToken()
        userNotificationsCenterDelegate.attachNotificationDelegate()
        notificationPermissionStatusSettingsObserver.startObserving()

        return true
    }

    // MARK: Responding to App Life-Cycle Events

    func applicationWillEnterForeground(_ application: UIApplication) {
        notificationsRegistrationService.renewAPNsDeviceToken()
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        notificationsService.removeDailyStudyReminderLocalNotifications()
    }

    // MARK: Handling Notifications

    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        notificationsRegistrationService.handleApplicationDidRegisterForRemoteNotificationsWithDeviceToken(deviceToken)
    }

    func application(
        _ application: UIApplication,
        didFailToRegisterForRemoteNotificationsWithError error: Error
    ) {
        notificationsRegistrationService.handleApplicationDidFailToRegisterForRemoteNotificationsWithError(error)
    }

    func application(
        _ application: UIApplication,
        didReceiveRemoteNotification userInfo: [AnyHashable: Any]
    ) {
        notificationsService.handleRemoteNotification(with: userInfo)
    }

    func application(_ application: UIApplication, didReceive notification: UILocalNotification) {
        notificationsService.handleLocalNotification(with: notification.userInfo)
    }

    // MARK: Opening a URL-Specified Resource

    func application(
        _ app: UIApplication,
        open url: URL,
        options: [UIApplication.OpenURLOptionsKey: Any] = [:]
    ) -> Bool {
        if GIDSignIn.sharedInstance.handle(url) {
            return true
        }

        return true
    }

    // MARK: Managing interface geometry

    func application(
        _ application: UIApplication,
        supportedInterfaceOrientationsFor window: UIWindow?
    ) -> UIInterfaceOrientationMask {
        interfaceOrientationManager.supportedInterfaceOrientations
    }
}
