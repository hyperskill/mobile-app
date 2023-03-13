import Foundation
import shared

enum StepQuizChildQuizType {
    case choice
    case code
    case sql
    case pycharm
    case matching
    case sorting
    case table
    case string
    case number
    case math
    case unsupported(blockName: String)

    var isCodeRelated: Bool {
        switch self {
        case .code, .sql, .pycharm:
            return true
        default:
            return false
        }
    }

    init(step: Step) {
        if StepQuizResolver.shared.isQuizSupportable(step: step) {
            switch step.block.name {
            case BlockName.shared.CHOICE:
                self = .choice
            case BlockName.shared.CODE:
                self = .code
            case BlockName.shared.SQL:
                self = .sql
            case BlockName.shared.PYCHARM:
                self = .pycharm
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
                self = .unsupported(blockName: step.block.name)
            }
        } else {
            self = .unsupported(blockName: step.block.name)
        }
    }
}
