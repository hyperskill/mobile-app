import Foundation
import shared

enum FillBlanksModeWrapper {
    case input
    case select
}

extension FillBlanksModeWrapper {
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

extension FillBlanksMode {
    var wrapped: FillBlanksModeWrapper? {
        FillBlanksModeWrapper(shared: self)
    }
}
