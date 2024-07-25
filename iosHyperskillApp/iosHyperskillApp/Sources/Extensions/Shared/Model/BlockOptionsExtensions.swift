import Foundation
import shared

extension Block.Options {
    // swiftlint:disable discouraged_optional_boolean
    convenience init(
        isMultipleChoice: Bool? = nil,
        language: String? = nil,
        isCheckbox: Bool? = nil,
        limits: [String: Limit]? = nil,
        codeTemplates: [String: String]? = nil,
        samples: [[String]]? = nil,
        files: [Block.OptionsFile]? = nil,
        codeBlanksStrings: [String]? = nil
    ) {
        let isMultipleChoice: KotlinBoolean? = {
            if let isMultipleChoice {
                return KotlinBoolean(value: isMultipleChoice)
            }
            return nil
        }()

        let isCheckbox: KotlinBoolean? = {
            if let isCheckbox {
                return KotlinBoolean(value: isCheckbox)
            }
            return nil
        }()

        self.init(
            isMultipleChoice: isMultipleChoice,
            language: language,
            isCheckbox: isCheckbox,
            limits: limits,
            codeTemplates: codeTemplates,
            samples: samples,
            files: files,
            codeBlanksStrings: codeBlanksStrings
        )
    }
    // swiftlint:enable discouraged_optional_boolean
}
