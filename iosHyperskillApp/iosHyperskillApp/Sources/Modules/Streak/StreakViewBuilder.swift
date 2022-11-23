import shared
import SwiftUI

final class StreakViewBuilder {
    private let streak: Streak
    private let viewType: ViewType

    private let formatter: Formatter

    init(streak: Streak, viewType: ViewType, formatter: Formatter = .default) {
        self.streak = streak
        self.viewType = viewType
        self.formatter = formatter
    }

    @ViewBuilder
    func build() -> some View {
        let currentStreakCountString = formatter.daysCount(streak.currentStreak)

        let daysStates = streak.history.reversed().map { historicalStreak -> StreakDayState in
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
                isNewStreakRecord: streak.isNewRecord,
                currentStreakCountString: currentStreakCountString,
                daysStates: daysStates
            )
        case .card:
            StreakCardView(
                isNewStreakRecord: streak.isNewRecord,
                currentStreakCountString: currentStreakCountString,
                daysStates: daysStates
            )
        }
    }

    enum ViewType {
        case plain
        case card
    }
}
