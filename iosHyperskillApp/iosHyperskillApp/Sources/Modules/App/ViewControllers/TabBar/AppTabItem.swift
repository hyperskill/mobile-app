import Foundation

enum AppTabItem: CaseIterable {
    // The order is important
    case home
    case studyPlan
    case leaderboard
    case profile
    case debug

    var title: String {
        switch self {
        case .home:
            Strings.TabBar.home
        case .studyPlan:
            Strings.TabBar.studyPlan
        case .leaderboard:
            Strings.TabBar.leaderboard
        case .profile:
            Strings.TabBar.profile
        case .debug:
            Strings.TabBar.debug
        }
    }

    var imageName: String {
        switch self {
        case .home:
            "dumbbell"
        case .studyPlan:
            "tab-bar-study-plan"
        case .leaderboard:
            "trophy"
        case .profile:
            "person"
        case .debug:
            "hammer.circle"
        }
    }

    var selectedImageName: String {
        switch self {
        case .home:
            "dumbbell.fill"
        case .studyPlan:
            "tab-bar-study-plan-filled"
        case .leaderboard:
            "trophy.fill"
        case .profile:
            "person.fill"
        case .debug:
            "hammer.circle.fill"
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
