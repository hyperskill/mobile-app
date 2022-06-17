import Foundation
import shared

enum StepQuizChildQuizType {
    case choice
    case unsupported(blockName: String)

    init(blockName: String) {
        switch blockName {
        case BlockName.shared.CHOICE:
            self = .choice
        default:
            self = .unsupported(blockName: blockName)
        }
    }
}
