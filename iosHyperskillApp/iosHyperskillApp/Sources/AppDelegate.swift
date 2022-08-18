import GoogleSignIn
import shared
import SwiftUI
import UIKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?

    // swiftlint:disable:next weak_delegate
    private lazy var userNotificationsCenterDelegate = UserNotificationsCenterDelegate()
    private lazy var notificationsService = NotificationsService()

    // MARK: Initializing the App

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        window = UIWindow(frame: UIScreen.main.bounds)

        let rootView = AppAssembly().makeModule()
        let rootViewController = UIHostingController(rootView: rootView)
        window?.rootViewController = rootViewController
        window?.makeKeyAndVisible()

        //AppAppearance.themeApplication()
        ApplicationThemeService.default.applyDefaultTheme()

        SentryManager.configure()
        ProgressHUD.configure()
        KeyboardManager.configure()
        NukeManager.registerCustomDecoders()

        userNotificationsCenterDelegate.attachNotificationDelegate()

        return true
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

    func applicationDidBecomeActive(_ application: UIApplication) {
        notificationsService.scheduleDailyStudyReminderLocalNotifications()
    }
}
