import Foundation
import shared

extension WelcomeFeatureStateKs: Equatable {
    public static func == (lhs: WelcomeFeatureStateKs, rhs: WelcomeFeatureStateKs) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle):
            return true
        case (.loading, .loading):
            return true
        case (.networkError, .networkError):
            return true
        case (.content(let lhsData), .content(let rhsData)):
            return lhsData.isEqual(rhsData)
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
        case (.loading, .networkError):
            return false
        case (.loading, .content):
            return false
        case (.idle, .loading):
            return false
        case (.idle, .networkError):
            return false
        case (.idle, .content):
            return false
        }
    }
}
