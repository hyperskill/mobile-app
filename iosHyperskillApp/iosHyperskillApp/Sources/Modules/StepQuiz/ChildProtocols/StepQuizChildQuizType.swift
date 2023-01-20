import Foundation
import shared

enum StepQuizChildQuizType {
    case choice
    case code
    case sql
    case matching
    case sorting
    case table
    case string
    case number
    case math
    case unsupported(blockName: String)

    var isCodeOrSQL: Bool {
        switch self {
        case .code, .sql:
            return true
        default:
            return false
        }
    }

    init(blockName: String) {
        if StepQuizResolver.shared.isQuizSupportable(blockName: blockName) {
            switch blockName {
            case BlockName.shared.CHOICE:
                self = .choice
            case BlockName.shared.CODE:
                self = .code
            case BlockName.shared.SQL:
                self = .sql
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
        } else {
            self = .unsupported(blockName: blockName)
        }
    }
}
