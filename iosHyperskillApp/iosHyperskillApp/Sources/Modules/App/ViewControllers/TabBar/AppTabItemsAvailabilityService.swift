import Foundation

protocol AppTabItemsAvailabilityServiceProtocol: AnyObject {
    func getAvailableTabs() -> [AppTabItem]
    func setIsMobileLeaderboardsEnabled(_ isEnabled: Bool)
}

final class AppTabItemsAvailabilityService: AppTabItemsAvailabilityServiceProtocol {
    static let shared = AppTabItemsAvailabilityService()

    private let isDebugModeAvailable: Bool
    private var isMobileLeaderboardsEnabled: Bool

    private init(
        isDebugModeAvailable: Bool = ApplicationInfo.isDebugModeAvailable,
        isMobileLeaderboardsEnabled: Bool = false
    ) {
        self.isDebugModeAvailable = isDebugModeAvailable
        self.isMobileLeaderboardsEnabled = isMobileLeaderboardsEnabled
    }

    func getAvailableTabs() -> [AppTabItem] {
        AppTabItem.allCases.filter { appTabItem in
            switch appTabItem {
            case .leaderboard:
                #if DEBUG
                true
                #else
                isMobileLeaderboardsEnabled
                #endif
            case .debug:
                isDebugModeAvailable
            default:
                true
            }
        }
    }

    func setIsMobileLeaderboardsEnabled(_ isEnabled: Bool) {
        isMobileLeaderboardsEnabled = isEnabled
    }
}
