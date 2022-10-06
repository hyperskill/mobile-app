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
        let viewControllers = AppTabItem.allCases.map { tabItem -> UIViewController in
            let rootViewController: UIViewController = {
                switch tabItem {
                case .home:
                    return HomeAssembly().makeModule()
                case .track:
                    return UIHostingController(rootView: TrackAssembly().makeModule())
                case .profile:
                    return UIHostingController(rootView: ProfileAssembly.currentUser().makeModule())
                }
            }()
            let navigationController = UINavigationController(rootViewController: rootViewController)
            navigationController.navigationBar.prefersLargeTitles = true

            let tabBarItem = UITabBarItem(
                title: tabItem.title,
                image: UIImage(named: tabItem.imageName),
                selectedImage: UIImage(named: tabItem.selectedImageName)
            )
            navigationController.tabBarItem = tabBarItem

            return navigationController
        }

        setViewControllers(viewControllers, animated: false)
    }
}

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
