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
            ? Strings.StepQuizString.Prompt.placeholder
            : Strings.StepQuizString.placeholder

        let isForceScoreCheckboxVisible = dataType == .prompt
        let isForceScoreCheckboxChecked = isForceScoreCheckboxVisible
            && (reply?.isPromptForceScoreCheckboxChecked() ?? false)

        return StepQuizStringViewData(
            text: text,
            placeholder: placeholder,
            isDecimalTextInput: dataType == .number,
            isForceScoreCheckboxVisible: isForceScoreCheckboxVisible,
            isForceScoreCheckboxChecked: isForceScoreCheckboxChecked
        )
    }
}
