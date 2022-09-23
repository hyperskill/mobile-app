import Foundation
import shared

enum StepQuizChildQuizType {
    case choice
    case code
    case matching
    case sorting
    case table
    case string
    case number
    case math
    case unsupported(blockName: String)

    init(blockName: String) {
        switch blockName {
        case BlockName.shared.CHOICE:
            self = .choice
        case BlockName.shared.CODE:
            self = .code
        case BlockName.shared.MATCHING:
            self = .matching
        case BlockName.shared.SORTING:
            self = .sorting
        case BlockName.shared.TABLE:
            self = .table
        case BlockName.shared.STRING:
            self = .string
        case BlockName.shared.NUMBER:
            self = .number
        case BlockName.shared.MATH:
            self = .math
        default:
            self = .unsupported(blockName: blockName)
        }
    }
}
