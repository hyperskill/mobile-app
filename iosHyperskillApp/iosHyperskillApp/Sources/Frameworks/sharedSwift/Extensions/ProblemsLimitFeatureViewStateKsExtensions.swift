import Foundation
import shared

extension ProblemsLimitFeatureViewStateKs: Equatable {
    public static func == (lhs: ProblemsLimitFeatureViewStateKs, rhs: ProblemsLimitFeatureViewStateKs) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle):
            return true
        case (.loading, .loading):
            return true
        case (.error, .error):
            return true
        case (.content(let lhsData), .content(let rhsData)):
            return ProblemsLimitFeatureViewStateContentKs(lhsData) == ProblemsLimitFeatureViewStateContentKs(rhsData)
        case (.content, .error):
            return false
        case (.content, .idle):
            return false
        case (.content, .loading):
            return false
        case (.error, .content):
            return false
        case (.error, .idle):
            return false
        case (.error, .loading):
            return false
        case (.idle, .content):
            return false
        case (.idle, .error):
            return false
        case (.idle, .loading):
            return false
        case (.loading, .content):
            return false
        case (.loading, .error):
            return false
        case (.loading, .idle):
            return false
        }
    }
}

extension ProblemsLimitFeatureViewStateContentKs: Equatable {
    public static func == (
        lhs: ProblemsLimitFeatureViewStateContentKs,
        rhs: ProblemsLimitFeatureViewStateContentKs
    ) -> Bool {
        switch (lhs, rhs) {
        case (.empty, .empty):
            return true
        case (.widget(let lhsData), .widget(let rhsData)):
            return lhsData == rhsData
        case (.empty, .widget):
            return false
        case (.widget, .empty):
            return false
        }
    }
}
