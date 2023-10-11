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
    private weak var appTabBarControllerDelegate: AppTabBarControllerDelegate?

    private var currentTabItem: AppTabItem

    init(
        initialTab: AppTabItem = .home,
        appTabBarControllerDelegate: AppTabBarControllerDelegate?
    ) {
        self.currentTabItem = initialTab
        self.appTabBarControllerDelegate = appTabBarControllerDelegate
        super.init(nibName: nil, bundle: nil)
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        setViewControllers()

        if let initialTabIndex = AppTabItem.availableItems.firstIndex(of: currentTabItem) {
            selectedIndex = initialTabIndex
        } else {
            selectedIndex = 0
        }

        delegate = self
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
