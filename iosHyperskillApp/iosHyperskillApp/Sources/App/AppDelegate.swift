import GoogleSignIn
import SVProgressHUD
import UIKit

class AppDelegate: NSObject, UIApplicationDelegate {
    /// `iosHyperskillApp.AppDelegate` is not direct `UIApplicationDelegate`, it's transferred via `UIApplicationDelegateAdaptor`
    /// to internal private `SwiftUI.AppDelegate`, which is real `UIApplicationDelegate` and which propagates delegate callbacks.
    private(set) static var shared: AppDelegate?

    var window: UIWindow?

    // MARK: Initializing the App

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        Self.shared = self

        KeyboardManager.configure()

        // TODO: Configure when window is assigned.
        // SVProgressHUD.setMinimumDismissTimeInterval(0.5)
        // SVProgressHUD.setDefaultMaskType(SVProgressHUDMaskType.clear)
        // SVProgressHUD.setDefaultStyle(SVProgressHUDStyle.light)
        // SVProgressHUD.setHapticsEnabled(true)

        return true
    }

    // MARK: Configuring and Discarding Scenes

    func application(
        _ application: UIApplication,
        configurationForConnecting connectingSceneSession: UISceneSession,
        options: UIScene.ConnectionOptions
    ) -> UISceneConfiguration {
        let sceneConfiguration = UISceneConfiguration(name: nil, sessionRole: connectingSceneSession.role)
        sceneConfiguration.delegateClass = SceneDelegate.self
        return sceneConfiguration
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

        return false
    }
}
