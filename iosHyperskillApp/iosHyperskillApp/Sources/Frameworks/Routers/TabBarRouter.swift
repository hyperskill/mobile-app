import UIKit

final class TabBarRouter: SourcelessRouter, RouterProtocol {
    private let tab: Tab
    private let popToRoot: Bool

    init(tab: Tab, popToRoot: Bool = true) {
        self.tab = tab
        self.popToRoot = popToRoot
    }

    func route() {
        currentTabBarController?.selectedIndex = tab.index

        guard popToRoot else {
            return
        }

        DispatchQueue.main.async {
            self.currentNavigation?.popToRootViewController(animated: true)
        }
    }

    enum Tab: Equatable {
        case home
        case track
        case profile

        var index: Int {
            switch self {
            case .home:
                return 0
            case .track:
                return 1
            case .profile:
                return 2
            }
        }
    }
}
