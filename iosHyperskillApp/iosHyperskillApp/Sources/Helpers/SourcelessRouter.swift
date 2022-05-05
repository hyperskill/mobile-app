import UIKit

class SourcelessRouter {
    var window: UIWindow? {
        (UIApplication.shared.delegate as? AppDelegate)?.window
    }

    func currentPresentedViewController() -> UIViewController? {
        guard let rootViewController = self.window?.rootViewController else {
            return nil
        }

        var result = rootViewController

        while let presentedViewController = result.presentedViewController {
            result = presentedViewController
        }

        return result
    }
}
