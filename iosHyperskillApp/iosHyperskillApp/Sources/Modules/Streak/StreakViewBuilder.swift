import shared
import SwiftUI

final class StreakViewBuilder {
    private let streak: Streak
    private let streakFreezeState: ProfileFeatureStreakFreezeState?
    private let onStreakFreezeTapped: () -> Void
    private let viewType: ViewType

    private let formatter: Formatter

    init(
        streak: Streak,
        streakFreezeState: ProfileFeatureStreakFreezeState?,
        onStreakFreezeTapped: () -> Void,
        viewType: ViewType,
        formatter: Formatter = .default
    ) {
        self.streak = streak
        self.streakFreezeState = streakFreezeState
        self.onStreakFreezeTapped = onStreakFreezeTapped
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

        let streakFreezeStateKs: ProfileFeatureStreakFreezeStateKs? = {
            guard let streakFreezeState else {
                return nil
            }
            return .init(streakFreezeState)
        }()

        switch viewType {
        case .plain:
            StreakView(
                isNewStreakRecord: streak.isNewRecord,
                currentStreakCountString: currentStreakCountString,
                daysStates: daysStates,
                streakFreezeState: streakFreezeStateKs,
                onStreakFreezeTapped: onStreakFreezeTapped
            )
        case .card:
            StreakCardView(
                isNewStreakRecord: streak.isNewRecord,
                currentStreakCountString: currentStreakCountString,
                daysStates: daysStates,
                streakFreezeState: streakFreezeStateKs,
                onStreakFreezeTapped: onStreakFreezeTapped
            )
        }
    }

    enum ViewType {
        case plain
        case card
    }
}
