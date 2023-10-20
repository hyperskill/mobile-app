import Foundation

struct StepQuizFillBlanksViewData: Hashable {
    var components: [StepQuizFillBlankComponent]
}

struct StepQuizFillBlankComponent: Hashable, Identifiable {
    let id: Int
    let type: StepQuizFillBlankComponentType
    var attributedText: NSAttributedString?
    var inputText: String?
}

enum StepQuizFillBlankComponentType {
    case text
    case input
}
