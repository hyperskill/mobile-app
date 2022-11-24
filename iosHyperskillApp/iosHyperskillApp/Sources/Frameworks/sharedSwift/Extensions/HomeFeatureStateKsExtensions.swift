import Foundation
import shared

#warning("TODO: Replce with source code generator Sourcery")
extension HomeFeatureStateKs: Equatable {
    public static func == (lhs: HomeFeatureStateKs, rhs: HomeFeatureStateKs) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle):
            return true
        case (.loading, .loading):
            return true
        case (.networkError, .networkError):
            return true
        case (.content(let lhsData), .content(let rhsData)):
            guard lhsData.isRefreshing == rhsData.isRefreshing else {
                return false
            }

            guard lhsData.recommendedRepetitionsCount == rhsData.recommendedRepetitionsCount else {
                return false
            }

            guard let lhsStreak = lhsData.streak,
                  let rhsStreak = rhsData.streak,
                  lhsStreak.isEqual(rhsStreak) else {
                return false
            }

            let lhsProblemOfDayStateKs = HomeFeatureProblemOfDayStateKs(lhsData.problemOfDayState)
            let rhsProblemOfDayStateKs = HomeFeatureProblemOfDayStateKs(rhsData.problemOfDayState)

            switch (lhsProblemOfDayStateKs, rhsProblemOfDayStateKs) {
            case (.empty, .empty):
                return true
            case (.needToSolve(let lhsData), .needToSolve(let rhsData)):
                return lhsData.isEqual(rhsData)
            case (.solved(let lhsData), .solved(let rhsData)):
                return lhsData.isEqual(rhsData)
            case (.solved, .empty):
                return false
            case (.solved, .needToSolve):
                return false
            case (.needToSolve, .empty):
                return false
            case (.needToSolve, .solved):
                return false
            case (.empty, .needToSolve):
                return false
            case (.empty, .solved):
                return false
            }
        case (.content, .idle):
            return false
        case (.content, .loading):
            return false
        case (.content, .networkError):
            return false
        case (.networkError, .idle):
            return false
        case (.networkError, .loading):
            return false
        case (.networkError, .content):
            return false
        case (.loading, .idle):
            return false
        case (.loading, .content):
            return false
        case (.loading, .networkError):
            return false
        case (.idle, .loading):
            return false
        case (.idle, .content):
            return false
        case (.idle, .networkError):
            return false
        }
    }
}
