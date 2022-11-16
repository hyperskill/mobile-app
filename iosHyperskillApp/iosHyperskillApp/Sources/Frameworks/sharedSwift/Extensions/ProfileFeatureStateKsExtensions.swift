import Foundation
import shared

#warning("TODO: Replce with source code generator Sourcery")
extension ProfileFeatureStateKs: Equatable {
    public static func == (lhs: ProfileFeatureStateKs, rhs: ProfileFeatureStateKs) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle):
            return true
        case (.loading, .loading):
            return true
        case (.content(let lhsData), .content(let rhsData)):
            return lhsData.isEqual(rhsData)
        case (.error, .error):
            return true
        case (.error, .idle):
            return false
        case (.error, .loading):
            return false
        case (.error, .content):
            return false
        case (.content, .idle):
            return false
        case (.content, .loading):
            return false
        case (.content, .error):
            return false
        case (.loading, .idle):
            return false
        case (.loading, .content):
            return false
        case (.loading, .error):
            return false
        case (.idle, .loading):
            return false
        case (.idle, .content):
            return false
        case (.idle, .error):
            return false
        }
    }
}
