import shared
import SwiftUI

struct StepQuizFeedbackView: View {
    let stepQuizFeedbackState: StepQuizFeedbackStateKs

    let onAction: (StepQuizFeedbackStateWrong.Action) -> Void

    var body: some View {
        switch stepQuizFeedbackState {
        case .idle, .unsupportedStep:
            EmptyView()
        case .correct(let correctState):
            StepQuizFeedbackStatusView(state: .correct)

            if let hint = correctState.hint {
                StepQuizFeedbackHintView(text: hint)
            }
        case .wrong(let wrongState):
            StepQuizFeedbackWrongStateView(
                state: wrongState,
                onAction: onAction
            )

            if let feedbackHint = wrongState.feedbackHint {
                StepQuizFeedbackHintView(text: feedbackHint)
            }
        case .rejectedSubmission(let rejectedSubmissionState):
            StepQuizFeedbackStatusView(
                state: .invalidReply(
                    message: rejectedSubmissionState.message
                )
            )
        case .evaluation:
            StepQuizFeedbackStatusView(state: .evaluation)
        case .validationFailed(let validationFailedState):
            StepQuizFeedbackStatusView(
                state: .invalidReply(
                    message: validationFailedState.message
                )
            )
        }
    }
}

#if DEBUG
#Preview {
    ScrollView {
        VStack {
            StepQuizFeedbackView(
                stepQuizFeedbackState: .correct(
                    StepQuizFeedbackStateCorrect(
                        hint: """
That's right! Since any comparison results in a boolean value, there is no need to write everything twice.
""",
                        useLatex: false
                    )
                ),
                onAction: { _ in }
            )

            StepQuizFeedbackView(
                stepQuizFeedbackState: .rejectedSubmission(
                    StepQuizFeedbackStateRejectedSubmission(
                        message: "Invalid reply to this step. Please try again."
                    )
                ),
                onAction: { _ in }
            )

            StepQuizFeedbackView(
                stepQuizFeedbackState: .evaluation,
                onAction: { _ in }
            )

            StepQuizFeedbackView(
                stepQuizFeedbackState: .validationFailed(
                    StepQuizFeedbackStateValidationFailed(message: "Invalid reply")
                ),
                onAction: { _ in }
            )
        }
        .padding()
    }
}
#endif
