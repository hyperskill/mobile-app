import Foundation
import shared

extension ChallengeWidgetViewStateKs: Equatable {
    public static func == (lhs: ChallengeWidgetViewStateKs, rhs: ChallengeWidgetViewStateKs) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle):
            true
        case (.empty, .empty):
            true
        case (.error, .error):
            true
        case (.loading(let lhsData), .loading(let rhsData)):
            lhsData.isEqual(rhsData)
        case (.content(let lhsSealedState), .content(let rhsSealedState)):
            ChallengeWidgetViewStateContentKs(lhsSealedState) == ChallengeWidgetViewStateContentKs(rhsSealedState)
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

extension ChallengeWidgetViewStateContentKs: Equatable {
    public static func == (lhs: ChallengeWidgetViewStateContentKs, rhs: ChallengeWidgetViewStateContentKs) -> Bool {
        switch (lhs, rhs) {
        case (.announcement(let lhsData), .announcement(let rhsData)):
            lhsData.isEqual(rhsData)
        case (.completed(let lhsData), .completed(let rhsData)):
            lhsData.isEqual(rhsData)
        case (.ended(let lhsData), .ended(let rhsData)):
            lhsData.isEqual(rhsData)
        case (.happeningNow(let lhsData), .happeningNow(let rhsData)):
            lhsData.isEqual(rhsData)
        case (.partiallyCompleted(let lhsData), .partiallyCompleted(let rhsData)):
            lhsData.isEqual(rhsData)
        case (.partiallyCompleted, .announcement):
            false
        case (.partiallyCompleted, .completed):
            false
        case (.partiallyCompleted, .ended):
            false
        case (.partiallyCompleted, .happeningNow):
            false
        case (.happeningNow, .announcement):
            false
        case (.happeningNow, .completed):
            false
        case (.happeningNow, .ended):
            false
        case (.happeningNow, .partiallyCompleted):
            false
        case (.ended, .announcement):
            false
        case (.ended, .completed):
            false
        case (.ended, .happeningNow):
            false
        case (.ended, .partiallyCompleted):
            false
        case (.completed, .announcement):
            false
        case (.completed, .ended):
            false
        case (.completed, .happeningNow):
            false
        case (.completed, .partiallyCompleted):
            false
        case (.announcement, .completed):
            false
        case (.announcement, .ended):
            false
        case (.announcement, .happeningNow):
            false
        case (.announcement, .partiallyCompleted):
            false
        }
    }
}
