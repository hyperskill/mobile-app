import Foundation

struct StepQuizFillBlanksViewData: Hashable {
    var components: [StepQuizFillBlankComponent]
}

struct StepQuizFillBlankComponent: Hashable, Identifiable {
    let id: Int
    let type: StepQuizFillBlankComponentType
    // text
    var attributedText: NSAttributedString?
    // input
    var inputText: String?
    var isFirstResponder = false
}

enum StepQuizFillBlankComponentType {
    case text
    case input
}
