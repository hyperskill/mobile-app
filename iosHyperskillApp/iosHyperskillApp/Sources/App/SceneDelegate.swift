import UIKit

class SceneDelegate: NSObject, UIWindowSceneDelegate {
    var window: UIWindow? {
        didSet {
            AppDelegate.shared?.window = self.window
        }
    }

    // MARK: Connecting and Disconnecting the Scene

    func scene(
        _ scene: UIScene,
        willConnectTo session: UISceneSession,
        options connectionOptions: UIScene.ConnectionOptions
    ) {
        guard let windowScene = scene as? UIWindowScene else {
            return
        }

        if #available(iOS 15.0, *) {
            self.window = windowScene.keyWindow
        } else {
            self.window = windowScene.windows.first(where: \.isKeyWindow)
        }
    }
}
