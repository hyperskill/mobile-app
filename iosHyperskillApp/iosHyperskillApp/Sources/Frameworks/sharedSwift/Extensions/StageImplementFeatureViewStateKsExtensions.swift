import Foundation
import shared

extension StageImplementFeatureViewStateKs: Equatable {
    public static func == (lhs: StageImplementFeatureViewStateKs, rhs: StageImplementFeatureViewStateKs) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle):
            return true
        case (.loading, .loading):
            return true
        case (.deprecated, .deprecated):
            return true
        case (.networkError, .networkError):
            return true
        case (.unsupported, .unsupported):
            return true
        case (.content(let lhsData), .content(let rhsData)):
            return lhsData.isEqual(rhsData)
        case (.content, .deprecated):
            return false
        case (.content, .idle):
            return false
        case (.content, .loading):
            return false
        case (.content, .networkError):
            return false
        case (.content, .unsupported):
            return false
        case (.unsupported, .content):
            return false
        case (.unsupported, .deprecated):
            return false
        case (.unsupported, .idle):
            return false
        case (.unsupported, .loading):
            return false
        case (.unsupported, .networkError):
            return false
        case (.networkError, .content):
            return false
        case (.networkError, .deprecated):
            return false
        case (.networkError, .idle):
            return false
        case (.networkError, .loading):
            return false
        case (.networkError, .unsupported):
            return false
        case (.deprecated, .content):
            return false
        case (.deprecated, .idle):
            return false
        case (.deprecated, .loading):
            return false
        case (.deprecated, .networkError):
            return false
        case (.deprecated, .unsupported):
            return false
        case (.loading, .content):
            return false
        case (.loading, .deprecated):
            return false
        case (.loading, .idle):
            return false
        case (.loading, .networkError):
            return false
        case (.loading, .unsupported):
            return false
        case (.idle, .content):
            return false
        case (.idle, .deprecated):
            return false
        case (.idle, .loading):
            return false
        case (.idle, .networkError):
            return false
        case (.idle, .unsupported):
            return false
        }
    }
}
