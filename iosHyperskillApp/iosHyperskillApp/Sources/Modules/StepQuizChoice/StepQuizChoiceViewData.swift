import Foundation

struct StepQuizChoiceViewData {
    let navigationTitle: String
    let desc: String

    let statsUsers: Int
    let statsHours: Int

    let isMultipleChoice: Bool
    var choices: [Choice]

    let quizStatus: QuizStatus?

    let feedbackText: String?

    var quizTitle: String {
        isMultipleChoice ? "Select one option from the list" : "Select one or more options from the list"
    }

    struct Choice {
        var text: String
        var isSelected: Bool
    }
}

#if DEBUG
extension StepQuizChoiceViewData {
    static func makePlaceholder(isMultipleChoice: Bool, quizStatus: QuizStatus? = nil) -> StepQuizChoiceViewData {
        let feedbackText: String? = {
            switch quizStatus {
            case .correct:
                return Strings.choiceQuizCorrectFeedbackText
            case .wrong:
                return Strings.choiceQuizWrongFeedbackText
            default:
                return nil
            }
        }()

        return StepQuizChoiceViewData(
            navigationTitle: "Problem's title",
            desc: "Select a statement that will throw an exception.",
            statsUsers: 2438,
            statsHours: 13,
            isMultipleChoice: isMultipleChoice,
            choices: [
                Choice(text: "char[] characters = new char[0];", isSelected: false),
                Choice(text: "char[] characters = new char[1];", isSelected: false),
                Choice(text: "char[] characters = new char[555];", isSelected: false),
                Choice(text: "char[] characters = new char[-5];", isSelected: false)
            ],
            quizStatus: quizStatus,
            feedbackText: feedbackText
        )
    }
}
#endif
