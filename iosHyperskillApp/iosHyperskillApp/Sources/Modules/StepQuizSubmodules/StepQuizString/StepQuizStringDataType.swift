import Foundation

enum StepQuizStringDataType {
    case string
    case number
    case math
    case prompt

    init?(quizType: StepQuizChildQuizType) {
        switch quizType {
        case .string:
            self = .string
        case .number:
            self = .number
        case .math:
            self = .math
        case .prompt:
            self = .prompt
        default:
            return nil
        }
    }
}
