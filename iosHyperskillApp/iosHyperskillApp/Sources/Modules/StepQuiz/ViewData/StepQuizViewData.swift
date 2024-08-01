import Foundation
import shared

struct StepQuizViewData: Equatable {
    let navigationTitle: String?

    let formattedStats: String?

    let stepTextHeaderTitle: String?
    let stepText: String?

    let quizType: StepQuizChildQuizType

    let quizName: String?

    let stepQuizFeedbackState: StepQuizFeedbackStateKs?
}
