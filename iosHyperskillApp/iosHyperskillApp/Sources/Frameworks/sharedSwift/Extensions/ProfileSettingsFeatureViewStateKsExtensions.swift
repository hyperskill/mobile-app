import Foundation
import shared

extension ProfileSettingsFeatureViewStateKs: Equatable {
    public static func == (lhs: ProfileSettingsFeatureViewStateKs, rhs: ProfileSettingsFeatureViewStateKs) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle):
            return true
        case (.loading, .loading):
            return true
        case (.content(let lhsData), .content(let rhsData)):
            return lhsData.isEqual(rhsData)
        case (.content, .idle):
            return false
        case (.content, .loading):
            return false
        case (.loading, .idle):
            return false
        case (.loading, .content):
            return false
        case (.idle, .loading):
            return false
        case (.idle, .content):
            return false
        }
    }
}
