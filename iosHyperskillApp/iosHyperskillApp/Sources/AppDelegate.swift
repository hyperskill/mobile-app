import GoogleSignIn
import SwiftUI
import UIKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?

    // swiftlint:disable:next weak_delegate
    private lazy var userNotificationsCenterDelegate = UserNotificationsCenterDelegate()

    // MARK: Initializing the App

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        self.window = UIWindow(frame: UIScreen.main.bounds)

        let rootView = AppAssembly().makeModule()
        let rootViewController = UIHostingController(rootView: rootView)
        self.window?.rootViewController = rootViewController
        self.window?.makeKeyAndVisible()

        //AppAppearance.themeApplication()

        SentryManager.configure()
        ProgressHUD.configure()
        KeyboardManager.configure()
        NukeManager.registerCustomDecoders()

        self.userNotificationsCenterDelegate.attachNotificationDelegate()

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
}
