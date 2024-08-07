import Foundation
import shared

enum StudyPlanWidgetViewStateSectionItemStateWrapper {
    case idle
    case next
    case skipped
    case completed
    case locked
}

extension StudyPlanWidgetViewStateSectionItemStateWrapper {
    init?(sharedItemState: StudyPlanWidgetViewStateSectionItemState) {
        switch sharedItemState {
        case .idle:
            self = .idle
        case .next:
            self = .next
        case .skipped:
            self = .skipped
        case .completed:
            self = .completed
        case .locked:
            self = .locked
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
