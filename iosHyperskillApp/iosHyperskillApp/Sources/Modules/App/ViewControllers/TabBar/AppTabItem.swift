import Foundation

enum AppTabItem: CaseIterable {
    case home
    case track
    case profile
    case debug

    var title: String {
        switch self {
        case .home:
            return Strings.TabBar.home
        case .track:
            return Strings.TabBar.track
        case .profile:
            return Strings.TabBar.profile
        case .debug:
            return Strings.TabBar.debug
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
        case .debug:
            return Images.SystemSymbol.Hammer.hammerCircle
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
        case .debug:
            return Images.SystemSymbol.Hammer.hammerCircleFill
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
