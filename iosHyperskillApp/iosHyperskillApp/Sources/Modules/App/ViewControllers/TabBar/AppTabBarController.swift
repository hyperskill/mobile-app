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
    private let availableTabs: [AppTabItem]

    init(
        initialTab: AppTabItem = .studyPlan,
        availableTabs: [AppTabItem] = AppTabItemsAvailabilityService.shared.getAvailableTabs(),
        appTabBarControllerDelegate: AppTabBarControllerDelegate?
    ) {
        self.currentTabItem = initialTab
        self.availableTabs = availableTabs
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

        if let initialTabIndex = availableTabs.firstIndex(of: currentTabItem) {
            selectedIndex = initialTabIndex
        } else {
            selectedIndex = 0
        }

        delegate = self
    }

    private func setViewControllers() {
        let viewControllers = availableTabs.map { tabItem -> UIViewController in
            let rootViewController: UIViewController = {
                switch tabItem {
                case .home:
                    HomeAssembly().makeModule()
                case .studyPlan:
                    StudyPlanAssembly().makeModule()
                case .leaderboard:
                    LeaderboardAssembly().makeModule()
                case .profile:
                    UIHostingController(rootView: ProfileAssembly.currentUser().makeModule())
                case .debug:
                    DebugAssembly().makeModule()
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
    var tabBarItem: UITabBarItem {
        switch self {
        case .studyPlan:
            UITabBarItem(
                title: title,
                image: UIImage(named: imageName),
                selectedImage: UIImage(named: selectedImageName)
            )
        case .home, .leaderboard, .profile, .debug:
            UITabBarItem(
                title: title,
                image: UIImage(systemName: imageName),
                selectedImage: UIImage(systemName: selectedImageName)
            )
        }
    }
}
