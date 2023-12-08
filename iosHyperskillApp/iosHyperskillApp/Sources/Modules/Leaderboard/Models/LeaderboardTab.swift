import Foundation
import shared

enum LeaderboardTab: Equatable, CaseIterable {
    case day
    case week

    var shared: LeaderboardScreenFeature.Tab {
        switch self {
        case .day:
            LeaderboardScreenFeature.Tab.day
        case .week:
            LeaderboardScreenFeature.Tab.week
        }
    }

    var title: String {
        switch self {
        case .day:
            Strings.Leaderboard.tabDay
        case .week:
            Strings.Leaderboard.tabWeek
        }
    }
}

extension LeaderboardTab {
    init?(sharedTab: LeaderboardScreenFeature.Tab) {
        switch sharedTab {
        case .day:
            self = .day
        case .week:
            self = .week
        default:
            assertionFailure("LeaderboardTab: Did receive unsupported type = \(sharedTab)")
            return nil
        }
    }
}

extension LeaderboardScreenFeature.Tab {
    var wrapped: LeaderboardTab? {
        LeaderboardTab(sharedTab: self)
    }
}
