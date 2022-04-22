import Foundation

enum AppTabItem {
    case home
    case settings

    var title: String {
        switch self {
        case .home:
            return NSLocalizedString("HomeTitle", comment: "")
        case .settings:
            return NSLocalizedString("SettingsTitle", comment: "")
        }
    }

    var imageSystemName: String {
        switch self {
        case .home:
            return "house"
        case .settings:
            return "gearshape"
        }
    }
}

final class AppNavigationState: ObservableObject {
    @Published var selectedTab = AppTabItem.home
}
