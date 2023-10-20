import Foundation

struct StepQuizFillBlanksViewData: Hashable {
    let components: [StepQuizFillBlankComponent]
}

struct StepQuizFillBlankComponent: Hashable, Identifiable {
    let id: Int
    let type: StepQuizFillBlankComponentType
    let text: String?
}

enum StepQuizFillBlankComponentType {
    case text
    case input
}
