import Foundation
import shared

extension TopicsRepetitionsFeatureStateKs: Equatable {
    public static func == (lhs: TopicsRepetitionsFeatureStateKs, rhs: TopicsRepetitionsFeatureStateKs) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle):
            return true
        case (.loading, .loading):
            return true
        case (.content(let lhsData), .content(let rhsData)):
            return lhsData.isEqual(rhsData)
        case (.networkError, .networkError):
            return true
        case (.networkError, .idle):
            return false
        case (.networkError, .loading):
            return false
        case (.networkError, .content):
            return false
        case (.content, .idle):
            return false
        case (.content, .loading):
            return false
        case (.content, .networkError):
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
