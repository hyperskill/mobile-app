import Foundation
import shared

enum FillBlanksModeWrapper {
    case input
    case select
}

extension FillBlanksModeWrapper {
    var sharedValue: FillBlanksMode {
        switch self {
        case .input:
            .input
        case .select:
            .select
        }
    }

    init?(shared: FillBlanksMode) {
        switch shared {
        case .input:
            self = .input
        case .select:
            self = .select
        default:
            assertionFailure("FillBlanksModeWrapper: Did receive unsupported FillBlanksMode = \(shared)")
            return nil
        }
    }
}
