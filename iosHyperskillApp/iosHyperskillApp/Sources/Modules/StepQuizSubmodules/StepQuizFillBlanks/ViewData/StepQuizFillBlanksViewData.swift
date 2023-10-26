import Foundation

struct StepQuizFillBlanksViewData: Hashable {
    var components: [StepQuizFillBlankComponent]
    var options: [StepQuizFillBlankOption]
}

struct StepQuizFillBlankComponent: Hashable, Identifiable {
    var id: Int = 0
    let type: ComponentType
    // text
    var attributedText: NSAttributedString?
    // input
    var inputText: String?
    var isFirstResponder = false
    // select
    var selectedOptionID: Int?

    enum ComponentType {
        case text
        case input
        case select
        case lineBreak
    }
}

struct StepQuizFillBlankOption: Hashable {
    let originalText: String
    let displayText: String
}
