import Foundation
import shared

extension PlaceholderNewUserFeatureStateKs: Equatable {
    public static func == (lhs: PlaceholderNewUserFeatureStateKs, rhs: PlaceholderNewUserFeatureStateKs) -> Bool {
        switch (lhs, rhs) {
        case (.content(let lhsData), .content(let rhsData)):
            return lhsData.isEqual(rhsData)
        }
    }
}
