import Foundation

struct StepQuizViewData {
    let formattedStats: String

    let stepText: String
    let stepQuizChildQuizType: StepQuizChildQuizType

    let quizName: String?

    let feedbackHintText: String?

    let stepHasHints: Bool
}
