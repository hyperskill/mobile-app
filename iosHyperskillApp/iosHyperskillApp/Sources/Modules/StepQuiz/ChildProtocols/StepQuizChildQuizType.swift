import Foundation
import shared

enum StepQuizChildQuizType: Equatable {
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
    case prompt
    case parsons
    case fillBlanks(mode: FillBlanksModeWrapper?)
    case unsupported(blockName: String)

    var isCodeRelated: Bool {
        switch self {
        case .code, .sql, .pycharm:
            true
        default:
            false
        }
    }

    var isUnsupported: Bool {
        if case .unsupported = self {
            true
        } else {
            false
        }
    }

    static func resolve(step: Step, datasetOrNil: Dataset?) -> StepQuizChildQuizType {
        StepQuizChildQuizType(step: step, datasetOrNil: datasetOrNil)
    }

    private init(step: Step, datasetOrNil: Dataset?) {
        let unsupportedQuizType = StepQuizChildQuizType.unsupported(blockName: step.block.name)

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
            case BlockName.shared.PROMPT:
                self = .prompt
            case BlockName.shared.PARSONS:
                self = .parsons
            case BlockName.shared.FILL_BLANKS:
                guard let dataset = datasetOrNil else {
                    self = .fillBlanks(mode: nil)
                    return
                }

                do {
                    let sharedMode = try FillBlanksResolver.shared.resolve(dataset: dataset)

                    switch FillBlanksModeWrapper(shared: sharedMode) {
                    case .input:
                        self = .fillBlanks(mode: .input)
                    case .select:
                        self = .fillBlanks(mode: .select)
                    default:
                        self = unsupportedQuizType
                    }
                } catch {
                    assertionFailure("FillBlanksResolver: failed to resolve fill blanks with error = \(error)")
                    self = unsupportedQuizType
                }
            default:
                self = unsupportedQuizType
            }
        } else {
            self = unsupportedQuizType
        }
    }
}
