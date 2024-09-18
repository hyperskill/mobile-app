import Foundation
import shared

extension StepQuizFeedbackStateHintKs: Equatable {
    public static func == (lhs: StepQuizFeedbackStateHintKs, rhs: StepQuizFeedbackStateHintKs) -> Bool {
        switch(lhs, rhs) {
        case (.fromSubmission(let lhsData), .fromSubmission(let rhsData)):
            lhsData.isEqual(rhsData)
        case (.fromRunCodeExecution(let lhs), .fromRunCodeExecution(let rhs)):
            StepQuizFeedbackStateHintFromRunCodeExecutionKs(lhs) == StepQuizFeedbackStateHintFromRunCodeExecutionKs(rhs)
        case (.fromRunCodeExecution, .fromSubmission):
            false
        case (.fromSubmission, .fromRunCodeExecution):
            false
        }
    }
}

extension StepQuizFeedbackStateHintFromRunCodeExecutionKs: Equatable {
    public static func == (
        lhs: StepQuizFeedbackStateHintFromRunCodeExecutionKs,
        rhs: StepQuizFeedbackStateHintFromRunCodeExecutionKs
    ) -> Bool {
        switch(lhs, rhs) {
        case (.loading, .loading):
            true
        case (.result(let lhsData), .result(let rhsData)):
            lhsData.isEqual(rhsData)
        case (.result, .loading):
            false
        case (.loading, .result):
            false
        }
    }
}
