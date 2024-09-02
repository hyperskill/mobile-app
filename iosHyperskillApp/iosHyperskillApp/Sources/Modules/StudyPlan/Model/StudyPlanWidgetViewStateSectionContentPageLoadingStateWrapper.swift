import Foundation
import shared

enum StudyPlanWidgetViewStateSectionContentPageLoadingStateWrapper {
    case hidden
    case loadMore
    case loading
}

extension StudyPlanWidgetViewStateSectionContentPageLoadingStateWrapper {
    init?(sharedState: StudyPlanWidgetViewStateSectionContentPageLoadingState) {
        switch sharedState {
        case .hidden:
            self = .hidden
        case .loadMore:
            self = .loadMore
        case .loading:
            self = .loading
        default:
            assertionFailure("Did receive unsupported item state = \(sharedState)")
            return nil
        }
    }
}

extension StudyPlanWidgetViewStateSectionContentPageLoadingState {
    var wrapped: StudyPlanWidgetViewStateSectionContentPageLoadingStateWrapper? {
        StudyPlanWidgetViewStateSectionContentPageLoadingStateWrapper(sharedState: self)
    }
}
