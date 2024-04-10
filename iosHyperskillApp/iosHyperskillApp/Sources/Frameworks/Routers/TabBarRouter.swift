import UIKit

final class TabBarRouter: SourcelessRouter, DeepLinkRouterProtocol {
    private let tab: AppTabItem
    private let popToRoot: Bool

    private let appTabItemsAvailabilityService: AppTabItemsAvailabilityServiceProtocol

    /// Initializes a new instance of a `TabBarRouter`.
    /// - Parameters:
    ///   - tab: Target tab to route to.
    ///   - popToRoot: Flag indicating whether to pop to root view controller of the target tab.
    init(
        tab: AppTabItem,
        popToRoot: Bool = true,
        appTabItemsAvailabilityService: AppTabItemsAvailabilityServiceProtocol = AppTabItemsAvailabilityService.shared
    ) {
        self.tab = tab
        self.popToRoot = popToRoot
        self.appTabItemsAvailabilityService = appTabItemsAvailabilityService
    }

    func route() {
        guard let targetTabIndex = index(of: tab) else {
            return
        }

        currentTabBarController?.selectedIndex = targetTabIndex

        guard popToRoot else {
            return
        }

        DispatchQueue.main.async {
            guard let currentNavigation = self.currentNavigation,
                  currentNavigation.viewControllers.count > 1 else {
                return
            }

            currentNavigation.popToRootViewController(animated: true)
        }
    }

    private func index(of tab: AppTabItem) -> Int? {
        appTabItemsAvailabilityService
            .getAvailableTabs()
            .firstIndex(of: tab)
    }
}
