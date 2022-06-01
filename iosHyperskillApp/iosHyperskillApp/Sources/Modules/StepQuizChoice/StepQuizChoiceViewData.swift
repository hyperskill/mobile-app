import Foundation
import shared

struct StepQuizChoiceViewData {
    let navigationTitle: String
    let desc: String

    let formattedStats: String

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
        let stepComponent = AppGraphBridge.sharedAppGraph.buildStepComponent()
        let statsTextMapper = stepComponent.stepQuizStatsTextMapper
        let formattedStats = statsTextMapper.getFormattedStepQuizStats(users: 2438, hours: 1)

        let feedbackText: String? = {
            switch quizStatus {
            case .correct:
                return """
That's right! Since any comparison results in a boolean value, there is no need to write everything twice.
"""
            case .wrong:
                return "Practice makes perfect. Let's learn from mistakes and try again."
            default:
                return nil
            }
        }()

        return StepQuizChoiceViewData(
            navigationTitle: "Problem's title",
            desc: "Select a statement that will throw an exception.",
            formattedStats: formattedStats,
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
