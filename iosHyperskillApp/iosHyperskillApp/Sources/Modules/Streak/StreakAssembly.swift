import shared
import SwiftUI

final class StreakAssembly {
    private let streak: Streak
    private let viewType: ViewType

    init(streak: Streak, viewType: ViewType) {
        self.streak = streak
        self.viewType = viewType
    }

    @ViewBuilder
    func makeModule() -> some View {
        let daysStates = streak.history.map { historicalStreak -> StreakDayState in
            if historicalStreak.isCompleted {
                return .active
            } else if historicalStreak.state == StreakState.frozen {
                return .frozen
            }
            return .passive
        }

        switch viewType {
        case .plain:
            StreakView(
                currentStreak: Int(streak.currentStreak),
                maxStreak: Int(streak.maxStreak),
                daysStates: daysStates
            )
        case .card:
            StreakCardView(
                currentStreak: Int(streak.currentStreak),
                maxStreak: Int(streak.maxStreak),
                daysStates: daysStates
            )
        }
    }

    enum ViewType {
        case plain
        case card
    }
}
