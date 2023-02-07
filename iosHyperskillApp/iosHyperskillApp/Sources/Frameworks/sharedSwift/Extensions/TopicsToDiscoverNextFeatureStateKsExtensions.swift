import Foundation
import shared

extension TopicsToDiscoverNextFeatureStateKs {
    var isEmpty: Bool {
        if case .empty = self {
            return true
        }
        return false
    }
}
