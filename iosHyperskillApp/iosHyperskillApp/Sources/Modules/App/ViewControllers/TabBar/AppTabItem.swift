import Foundation

enum AppTabItem: CaseIterable {
    case home
    case studyPlan
    case profile
    case debug

    var title: String {
        switch self {
        case .home:
            return Strings.TabBar.home
        case .studyPlan:
            return Strings.TabBar.studyPlan
        case .profile:
            return Strings.TabBar.profile
        case .debug:
            return Strings.TabBar.debug
        }
    }

    var imageName: String {
        switch self {
        case .home:
            return "dumbbell"
        case .studyPlan:
            return "tab-bar-study-plan"
        case .profile:
            return "person"
        case .debug:
            return "hammer.circle"
        }
    }

    var selectedImageName: String {
        switch self {
        case .home:
            return "dumbbell.fill"
        case .studyPlan:
            return "tab-bar-study-plan-filled"
        case .profile:
            return "person.fill"
        case .debug:
            return "hammer.circle.fill"
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
