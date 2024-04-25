import Foundation
import shared

enum StepTypeWrapper {
    case theory
    case practice
}

extension StepTypeWrapper {
    var sharedValue: Step.Type_ {
        switch self {
        case .theory:
            .theory
        case .practice:
            .practice
        }
    }

    init(shared: Step.Type_) {
        switch shared {
        case .theory:
            self = .theory
        case .practice:
            self = .practice
        default:
            fatalError("StepTypeWrapper: Did receive unsupported Step.Type_ = \(shared)")
        }
    }
}

extension Step.Type_ {
    var wrapped: StepTypeWrapper {
        StepTypeWrapper(shared: self)
    }
}
