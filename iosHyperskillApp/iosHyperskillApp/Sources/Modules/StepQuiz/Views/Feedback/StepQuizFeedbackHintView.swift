import shared
import SwiftUI

struct StepQuizFeedbackHintView: View {
    let hint: StepQuizFeedbackStateHint

    var body: some View {
        switch StepQuizFeedbackStateHintKs(hint) {
        case .fromSubmission(let data):
            StepQuizSubmissionFeedbackHintView(text: data.text)
        case .fromRunCodeExecution(let runCodeExecution):
            StepQuizRunCodeFeedbackHintView(
                runCodeExecution: StepQuizFeedbackStateHintFromRunCodeExecutionKs(runCodeExecution)
            )
        }
    }
}

extension StepQuizFeedbackHintView: Equatable {
    static func == (lhs: StepQuizFeedbackHintView, rhs: StepQuizFeedbackHintView) -> Bool {
        StepQuizFeedbackStateHintKs(lhs.hint) == StepQuizFeedbackStateHintKs(rhs.hint)
    }
}
