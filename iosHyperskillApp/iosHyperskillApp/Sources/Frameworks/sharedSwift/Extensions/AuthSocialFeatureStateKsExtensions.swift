import Foundation
import shared

extension AuthSocialFeatureStateKs: Equatable {
    public static func == (lhs: AuthSocialFeatureStateKs, rhs: AuthSocialFeatureStateKs) -> Bool {
        switch(lhs, rhs) {
        case (.idle, .idle):
            true
        case(.loading, .loading) :
            true
        case(.error(let lhsData), .error(let rhsData)):
            lhsData.isEqual(rhsData)
        case(.authenticated, .authenticated):
            true
        case(.idle, .loading):
            false
        case(.idle, .error):
            false
        case(.idle, .authenticated):
            false
        case(.loading, .idle):
            false
        case(.loading, .error):
            false
        case(.loading, .authenticated):
            false
        case(.error, .idle):
            false
        case(.error, .loading):
            false
        case(.error, .authenticated):
            false
        case(.authenticated, .idle):
            false
        case(.authenticated, .loading):
            false
        case(.authenticated, .error):
            false
        }
    }
}
