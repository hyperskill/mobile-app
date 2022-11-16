import Foundation
import shared

#warning("TODO: Replce with source code generator Sourcery")
extension AppFeatureStateKs: Equatable {
    public static func == (lhs: AppFeatureStateKs, rhs: AppFeatureStateKs) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle):
            return true
        case (.loading, .loading):
            return true
        case (.networkError, .networkError):
            return true
        case (.ready(let lhsData), .ready(let rhsData)):
            return lhsData.isEqual(rhsData)
        case (.ready, .idle):
            return false
        case (.ready, .loading):
            return false
        case (.ready, .networkError):
            return false
        case (.networkError, .idle):
            return false
        case (.networkError, .loading):
            return false
        case (.networkError, .ready):
            return false
        case (.loading, .idle):
            return false
        case (.loading, .networkError):
            return false
        case (.loading, .ready):
            return false
        case (.idle, .loading):
            return false
        case (.idle, .networkError):
            return false
        case (.idle, .ready):
            return false
        }
    }
}
