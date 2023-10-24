import Foundation

struct StepQuizFillBlanksViewData: Hashable {
    var components: [StepQuizFillBlankComponent]
}

struct StepQuizFillBlankComponent: Hashable, Identifiable {
    var id: Int = 0
    let type: ComponentType
    // text
    var attributedText: NSAttributedString?
    // input
    var inputText: String?
    var isFirstResponder = false

    enum ComponentType {
        case text
        case input
        case lineBreak
    }
}
