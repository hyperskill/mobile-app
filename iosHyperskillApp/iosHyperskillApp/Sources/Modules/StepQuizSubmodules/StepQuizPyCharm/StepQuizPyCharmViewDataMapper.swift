import Foundation
import shared

final class StepQuizPyCharmViewDataMapper: StepQuizCodeViewDataMapper {
    override func mapCodeDataToViewData(step: Step, reply: Reply?) -> StepQuizCodeViewData {
        let blockOptions = step.block.options

        let languageStringValue = blockOptions.language
        let language: CodeLanguage? = {
            if let languageStringValue {
                return CodeLanguage(rawValue: languageStringValue)
            }
            return nil
        }()

        let codeTemplate = blockOptions.files?.first(where: { $0.isVisible })?.text
        let code: String? = {
            if let solution = reply?.solution {
                return solution.first(where: { $0.isVisible })?.text
            }
            return codeTemplate
        }()

        return StepQuizCodeViewData(
            language: language,
            languageStringValue: languageStringValue,
            code: code,
            codeTemplate: codeTemplate,
            samples: [],
            stepText: step.block.text
        )
    }
}
