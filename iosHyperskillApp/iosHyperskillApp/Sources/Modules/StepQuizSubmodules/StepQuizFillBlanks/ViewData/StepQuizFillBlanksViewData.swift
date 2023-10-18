import Foundation

struct StepQuizFillBlanksViewData: Hashable {
    let items: [StepQuizFillBlankItem]
}

enum StepQuizFillBlankItem: Hashable {
    case text(String)
    case input(String?)
}
