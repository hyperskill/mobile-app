import UIKit

extension UIApplication {
    var currentKeyWindow: UIWindow? {
        UIApplication
            .shared
            .connectedScenes
            .filter { $0.activationState == .foregroundActive }
            .compactMap { $0 as? UIWindowScene }
            .first?
            .windows
            .first(where: { $0.isKeyWindow })
    }

    var currentRootViewController: UIViewController? {
        self.currentKeyWindow?.rootViewController
    }
}
