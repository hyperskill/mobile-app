import Foundation
import shared

enum StepQuizStringViewDataMapper {
    static func map(dataType: StepQuizStringDataType, reply: Reply?) -> StepQuizStringViewData {
        let text: String = { () -> String? in
            guard let reply else {
                return nil
            }

            switch dataType {
            case .string:
                return reply.text
            case .number:
                return reply.number
            case .math:
                return reply.formula
            case .prompt:
                return reply.prompt
            }
        }() ?? ""

        let placeholder = dataType == .prompt
            ? Strings.StepQuizString.promptPlaceholder
            : Strings.StepQuizString.defaultPlaceholder

        return StepQuizStringViewData(
            text: text,
            placeholder: placeholder,
            isDecimalTextInput: dataType == .number
        )
    }
}
