import Foundation
import shared

extension StepQuizFeedbackStateKs: Equatable {
    public static func == (lhs: StepQuizFeedbackStateKs, rhs: StepQuizFeedbackStateKs) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle):
            true
        case (.evaluation, .evaluation):
            true
        case (.unsupportedStep, .unsupportedStep):
            true
        case (.correct(let lhsData), .correct(let rhsData)):
            lhsData.isEqual(rhsData)
        case (.wrong(let lhsData), .wrong(let rhsData)):
            lhsData.isEqual(rhsData)
        case (.rejectedSubmission(let lhsData), .rejectedSubmission(let rhsData)):
            lhsData.isEqual(rhsData)
        case (.validationFailed(let lhsData), .validationFailed(let rhsData)):
            lhsData.isEqual(rhsData)
        default:
            false
        }
    }
}
