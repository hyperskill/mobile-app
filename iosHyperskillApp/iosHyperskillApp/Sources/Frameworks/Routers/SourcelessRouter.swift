import UIKit

class SourcelessRouter {
    var window: UIWindow? {
        (UIApplication.shared.delegate as? AppDelegate)?.window
    }

    var currentTabBarController: UITabBarController? {
        window?.rootViewController?.children.first as? UITabBarController
    }

    var currentNavigation: UINavigationController? {
        guard let tabController = currentTabBarController else {
            return nil
        }

        let count = tabController.viewControllers?.count ?? 0
        let index = tabController.selectedIndex

        let tabHostingController: UIViewController? = {
            if index < count {
                return tabController.viewControllers?[tabController.selectedIndex]
            } else {
                return tabController.viewControllers?[0]
            }
        }()

        return tabHostingController?.children.first as? UINavigationController
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
