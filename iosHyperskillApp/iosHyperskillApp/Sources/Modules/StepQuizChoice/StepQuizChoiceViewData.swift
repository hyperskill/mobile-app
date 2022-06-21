import Foundation

struct StepQuizChoiceViewData {
    let isMultipleChoice: Bool
    var choices: [Choice]

    struct Choice {
        var text: String
        var isSelected: Bool
    }
}
