import shared
import SwiftUI

final class StreakAssembly: Assembly {
    private let streak: Streak

    init(streak: Streak) {
        self.streak = streak
    }

    func makeModule() -> StreakCardView {
        let daysStates = streak.history.map { historicalStreak -> StreakDayState in
            if historicalStreak.isCompleted {
                return .active
            } else if historicalStreak.state == StreakState.frozen {
                return .frozen
            }
            return .passive
        }

        return StreakCardView(
            currentStreak: Int(streak.currentStreak),
            maxStreak: Int(streak.maxStreak),
            daysStates: daysStates
        )
    }
}
