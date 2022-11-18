import Foundation
import shared

#warning("TODO: Replce with source code generator Sourcery")
extension StepFeatureStateKs: Equatable {
    public static func == (lhs: StepFeatureStateKs, rhs: StepFeatureStateKs) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle):
            return true
        case (.loading, .loading):
            return true
        case (.error, .error):
            return true
        case (.data(let lhsData), .data(let rhsData)):
            return lhsData.isEqual(rhsData)
        case (.data, .idle):
            return false
        case (.data, .loading):
            return false
        case (.data, .error):
            return false
        case (.error, .idle):
            return false
        case (.error, .loading):
            return false
        case (.error, .data):
            return false
        case (.loading, .idle):
            return false
        case (.loading, .error):
            return false
        case (.loading, .data):
            return false
        case (.idle, .loading):
            return false
        case (.idle, .error):
            return false
        case (.idle, .data):
            return false
        }
    }
}
