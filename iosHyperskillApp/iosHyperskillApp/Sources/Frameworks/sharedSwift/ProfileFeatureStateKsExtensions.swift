import Foundation
import shared

extension ProfileFeatureStateKs: Equatable {
    public static func == (lhs: ProfileFeatureStateKs, rhs: ProfileFeatureStateKs) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle):
            return false
        case (.loading, .loading):
            return false
        case (.content(let oldContent), .content(let newContent)):
            return !oldContent.isEqual(newContent)
        case (.error, .error):
            return false
        case (.error, .idle):
            return true
        case (.error, .loading):
            return true
        case (.error, .content):
            return true
        case (.content, .idle):
            return true
        case (.content, .loading):
            return true
        case (.content, .error):
            return true
        case (.loading, .idle):
            return true
        case (.loading, .content):
            return true
        case (.loading, .error):
            return true
        case (.idle, .loading):
            return true
        case (.idle, .content):
            return true
        case (.idle, .error):
            return true
        }
    }
}
