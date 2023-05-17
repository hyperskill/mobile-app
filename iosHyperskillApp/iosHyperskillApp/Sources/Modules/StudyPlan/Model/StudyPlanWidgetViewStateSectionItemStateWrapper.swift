import Foundation
import shared

enum StudyPlanWidgetViewStateSectionItemStateWrapper {
    case idle
    case next
    case locked
    case skipped
    case completed
}

extension StudyPlanWidgetViewStateSectionItemStateWrapper {
    init?(sharedItemState: StudyPlanWidgetViewStateSectionItemState) {
        switch sharedItemState {
        case .idle:
            self = .idle
        case .next:
            self = .next
        case .locked:
            self = .locked
        case .skipped:
            self = .skipped
        case .completed:
            self = .completed
        default:
            assertionFailure("Did receive unsupported item state type = \(sharedItemState)")
            return nil
        }
    }
}

extension StudyPlanWidgetViewStateSectionItemState {
    var wrapped: StudyPlanWidgetViewStateSectionItemStateWrapper? {
        StudyPlanWidgetViewStateSectionItemStateWrapper(sharedItemState: self)
    }
}
