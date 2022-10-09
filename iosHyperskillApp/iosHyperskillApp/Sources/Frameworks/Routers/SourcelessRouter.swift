import UIKit

class SourcelessRouter {
    var window: UIWindow? {
        (UIApplication.shared.delegate as? AppDelegate)?.window
    }

    var currentTabBarController: UITabBarController? {
        for childrenViewController in window?.rootViewController?.children ?? [] {
            if let tabBarController = childrenViewController as? UITabBarController {
                return tabBarController
            }
        }
        return nil
    }

    var currentNavigation: UINavigationController? {
        guard let tabController = currentTabBarController else {
            return nil
        }

        let count = tabController.viewControllers?.count ?? 0
        let index = tabController.selectedIndex

        guard index < count else {
            return nil
        }

        return tabController.children[index] as? UINavigationController
    }

    func currentPresentedViewController() -> UIViewController? {
        guard let rootViewController = window?.rootViewController else {
            return nil
        }

        var result = rootViewController

        while let presentedViewController = result.presentedViewController {
            result = presentedViewController
        }

        return result
    }
}
