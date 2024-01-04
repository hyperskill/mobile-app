import Foundation
import shared

extension InterviewPreparationWidgetViewStateKs: Equatable {
    public static func == (
        lhs: InterviewPreparationWidgetViewStateKs,
        rhs: InterviewPreparationWidgetViewStateKs
    ) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle):
            true
        case (.empty, .empty):
            true
        case (.error, .error):
            true
        case (.loading(let lhsData), .loading(let rhsData)):
            lhsData.isEqual(rhsData)
        case (.content(let lhsData), .content(let rhsData)):
            lhsData.isEqual(rhsData)
        case (.content, .empty):
            false
        case (.content, .error):
            false
        case (.content, .idle):
            false
        case (.content, .loading):
            false
        case (.loading, .content):
            false
        case (.loading, .empty):
            false
        case (.loading, .error):
            false
        case (.loading, .idle):
            false
        case (.error, .content):
            false
        case (.error, .empty):
            false
        case (.error, .idle):
            false
        case (.error, .loading):
            false
        case (.empty, .content):
            false
        case (.empty, .error):
            false
        case (.empty, .idle):
            false
        case (.empty, .loading):
            false
        case (.idle, .content):
            false
        case (.idle, .empty):
            false
        case (.idle, .error):
            false
        case (.idle, .loading):
            false
        }
    }
}
