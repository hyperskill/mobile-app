import UIKit

class SceneDelegate: NSObject, UIWindowSceneDelegate {
    var window: UIWindow?

    // MARK: Connecting and Disconnecting the Scene

    func scene(
        _ scene: UIScene,
        willConnectTo session: UISceneSession,
        options connectionOptions: UIScene.ConnectionOptions
    ) {
        guard let windowScene = scene as? UIWindowScene else {
            return
        }

        let keyWindow: UIWindow? = {
            if #available(iOS 15.0, *) {
                return windowScene.keyWindow
            } else {
                return windowScene.windows.first(where: \.isKeyWindow)
            }
        }()

        self.window = keyWindow
        AppDelegate.shared?.window = keyWindow
    }
}
