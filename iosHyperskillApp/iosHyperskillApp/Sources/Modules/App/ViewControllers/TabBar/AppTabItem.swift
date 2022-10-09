import Foundation

enum AppTabItem: CaseIterable {
    case home
    case track
    case profile

    var title: String {
        switch self {
        case .home:
            return Strings.TabBar.home
        case .track:
            return Strings.TabBar.track
        case .profile:
            return Strings.TabBar.profile
        }
    }

    var imageName: String {
        switch self {
        case .home:
            return Images.TabBar.home
        case .track:
            return Images.TabBar.track
        case .profile:
            return Images.TabBar.profile
        }
    }

    var selectedImageName: String {
        switch self {
        case .home:
            return Images.TabBar.homeFilled
        case .track:
            return Images.TabBar.trackFilled
        case .profile:
            return Images.TabBar.profileFilled
        }
    }
}

extension AppTabItem {
    init?(index: Int) {
        let targetTabItemInfo = AppTabItem.allCases.enumerated().first { (tabItemIndex, _) in
            if index == tabItemIndex {
                return true
            }
            return false
        }

        if let targetTabItemInfo {
            self = targetTabItemInfo.element
        } else {
            return nil
        }
    }
}
