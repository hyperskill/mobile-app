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
        codeBlanksStrings: [String]? = nil,
        codeBlanksVariables: [String]? = nil,
        codeBlanksOperations: [String]? = nil,
        codeBlanksAvailableConditions: Set<String>? = nil,
        codeBlanksEnabled: Bool? = nil,
        codeBlanksTemplateString: String? = nil
    ) {
        self.init(
            isMultipleChoice: isMultipleChoice.flatMap(KotlinBoolean.init(value:)),
            language: language,
            isCheckbox: isCheckbox.flatMap(KotlinBoolean.init(value:)),
            limits: limits,
            codeTemplates: codeTemplates,
            internalSamples: samples,
            files: files,
            codeBlanksStrings: codeBlanksStrings,
            codeBlanksVariables: codeBlanksVariables,
            codeBlanksOperations: codeBlanksOperations,
            codeBlanksAvailableConditions: codeBlanksAvailableConditions,
            codeBlanksEnabled: codeBlanksEnabled.flatMap(KotlinBoolean.init(value:)),
            codeBlanksTemplateString: codeBlanksTemplateString
        )
    }
    // swiftlint:enable discouraged_optional_boolean
}
