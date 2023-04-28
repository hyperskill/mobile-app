import SwiftUI
import UIKit

protocol AppTabBarControllerDelegate: AnyObject {
    func appTabBarController(
        _ controller: AppTabBarController,
        didSelectTabItem newTabItem: AppTabItem,
        oldTabItem: AppTabItem
    )
}

final class AppTabBarController: UITabBarController {
    weak var appTabBarControllerDelegate: AppTabBarControllerDelegate?

    private var currentTabItem = AppTabItem.home

    override func viewDidLoad() {
        super.viewDidLoad()
        delegate = self
        setViewControllers()
    }

    private func setViewControllers() {
        let viewControllers = AppTabItem.availableItems.map { tabItem -> UIViewController in
            let rootViewController: UIViewController = {
                switch tabItem {
                case .home:
                    return HomeAssembly().makeModule()
                case .studyPlan:
                    return StudyPlanAssembly().makeModule()
                case .profile:
                    return UIHostingController(rootView: ProfileAssembly.currentUser().makeModule())
                case .debug:
                    return DebugAssembly().makeModule()
                }
            }()

            let navigationController = UINavigationController(rootViewController: rootViewController)
            navigationController.navigationBar.prefersLargeTitles = true
            navigationController.tabBarItem = tabItem.tabBarItem

            return navigationController
        }

        setViewControllers(viewControllers, animated: false)
    }
}

// MARK: - AppTabBarController: UITabBarControllerDelegate -

extension AppTabBarController: UITabBarControllerDelegate {
    func tabBarController(_ tabBarController: UITabBarController, didSelect viewController: UIViewController) {
        guard let selectedTabItem = AppTabItem(index: selectedIndex) else {
            return
        }

        appTabBarControllerDelegate?.appTabBarController(
            self,
            didSelectTabItem: selectedTabItem,
            oldTabItem: currentTabItem
        )

        currentTabItem = selectedTabItem
    }
}

// MARK: - AppTabItem (UITabBarItem) -

private extension AppTabItem {
    static var availableItems: [AppTabItem] {
        var result = AppTabItem.allCases

        if !ApplicationInfo.isDebugModeAvailable {
            result.removeAll(where: { $0 == .debug })
        }

        return result
    }

    var tabBarItem: UITabBarItem {
        switch self {
        case .home, .studyPlan, .profile:
            return UITabBarItem(
                title: title,
                image: UIImage(named: imageName),
                selectedImage: UIImage(named: selectedImageName)
            )
        case .debug:
            return UITabBarItem(
                title: title,
                image: UIImage(systemName: imageName),
                selectedImage: UIImage(systemName: selectedImageName)
            )
        }
    }
}
