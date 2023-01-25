import Foundation
import shared

extension StepQuizHintsViewStateKs: Equatable {
    public static func == (lhs: StepQuizHintsViewStateKs, rhs: StepQuizHintsViewStateKs) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle):
            return true
        case (.initialLoading, .initialLoading):
            return true
        case (.hintLoading, .hintLoading):
            return true
        case (.error, .error):
            return true
        case (.content(let lhsData), .content(let rhsData)):
            return StepQuizHintsViewStateContentKs(lhsData) == StepQuizHintsViewStateContentKs(rhsData)
        case (.content, .error):
            return false
        case (.content, .hintLoading):
            return false
        case (.content, .idle):
            return false
        case (.content, .initialLoading):
            return false
        case (.error, .content):
            return false
        case (.error, .hintLoading):
            return false
        case (.error, .idle):
            return false
        case (.error, .initialLoading):
            return false
        case (.hintLoading, .content):
            return false
        case (.hintLoading, .error):
            return false
        case (.hintLoading, .idle):
            return false
        case (.hintLoading, .initialLoading):
            return false
        case (.initialLoading, .content):
            return false
        case (.initialLoading, .error):
            return false
        case (.initialLoading, .hintLoading):
            return false
        case (.initialLoading, .idle):
            return false
        case (.idle, .content):
            return false
        case (.idle, .error):
            return false
        case (.idle, .hintLoading):
            return false
        case (.idle, .initialLoading):
            return false
        }
    }
}

extension StepQuizHintsViewStateContentKs: Equatable {
    public static func == (lhs: StepQuizHintsViewStateContentKs, rhs: StepQuizHintsViewStateContentKs) -> Bool {
        switch (lhs, rhs) {
        case (.seeHintButton, .seeHintButton):
            return true
        case (.hintCard(let lhsData), .hintCard(let rhsData)):
            return lhsData.isEqual(rhsData)
        case (.hintCard, .seeHintButton):
            return false
        case (.seeHintButton, .hintCard):
            return false
        }
    }
}
