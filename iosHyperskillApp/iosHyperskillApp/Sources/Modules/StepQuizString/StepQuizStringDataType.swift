import Foundation

enum StepQuizStringDataType {
    case string
    case number
    case math

    init?(quizType: StepQuizChildQuizType) {
        switch quizType {
        case .string:
            self = .string
        case .number:
            self = .number
        case .math:
            self = .math
        default:
            return nil
        }
    }
}
