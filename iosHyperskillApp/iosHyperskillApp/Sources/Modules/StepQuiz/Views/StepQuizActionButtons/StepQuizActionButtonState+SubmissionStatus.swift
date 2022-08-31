import Foundation
import shared

extension StepQuizActionButton.State {
    init(submissionStatus submissionStatusOrNil: SubmissionStatus?) {
        let submissionStatus = submissionStatusOrNil ?? SubmissionStatus.local

        switch submissionStatus {
        case SubmissionStatus.evaluation:
            self = .evaluation
        case SubmissionStatus.wrong:
            self = .wrong
        case SubmissionStatus.correct:
            self = .correct
        case SubmissionStatus.outdated:
            self = .wrong
        case SubmissionStatus.local:
            self = .normal
        default:
            self = .normal
        }
    }
}
