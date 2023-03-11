import Foundation
import shared

extension Block.Options {
    convenience init(
        isMultipleChoice: Bool? = nil,
        language: String? = nil,
        isCheckbox: Bool? = nil,
        executionTimeLimit: Int? = nil,
        executionMemoryLimit: Int? = nil,
        limits: [String: Limit]? = nil,
        codeTemplates: [String: String]? = nil,
        samples: [[String]]? = nil,
        files: [Block.OptionsFile]? = nil
    ) {
        let isMultipleChoice: KotlinBoolean? = {
            if let isMultipleChoice = isMultipleChoice {
                return KotlinBoolean(value: isMultipleChoice)
            }
            return nil
        }()

        let isCheckbox: KotlinBoolean? = {
            if let isCheckbox = isCheckbox {
                return KotlinBoolean(value: isCheckbox)
            }
            return nil
        }()

        let executionTimeLimit: KotlinInt? = {
            if let executionTimeLimit = executionTimeLimit {
                return KotlinInt(value: Int32(executionTimeLimit))
            }
            return nil
        }()

        let executionMemoryLimit: KotlinInt? = {
            if let executionMemoryLimit = executionMemoryLimit {
                return KotlinInt(value: Int32(executionMemoryLimit))
            }
            return nil
        }()

        self.init(
            isMultipleChoice: isMultipleChoice,
            language: language,
            isCheckbox: isCheckbox,
            executionTimeLimit: executionTimeLimit,
            executionMemoryLimit: executionMemoryLimit,
            limits: limits,
            codeTemplates: codeTemplates,
            samples: samples,
            files: files
        )
    }
}
