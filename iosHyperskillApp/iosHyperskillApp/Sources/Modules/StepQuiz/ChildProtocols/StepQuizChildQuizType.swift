import Foundation
import shared

enum StepQuizChildQuizType {
    case choice
    case matching
    case unsupported(blockName: String)

    init(blockName: String) {
        switch blockName {
        case BlockName.shared.CHOICE:
            self = .choice
        case BlockName.shared.MATCHING:
            self = .matching
        default:
            self = .unsupported(blockName: blockName)
        }
    }
}
