import Foundation
import shared

final class StepQuizSQLViewDataMapper: StepQuizCodeViewDataMapper {
    override func mapCodeDataToViewData(step: Step, reply: Reply?) -> StepQuizCodeViewData {
        StepQuizCodeViewData(
            language: .sql,
            languageStringValue: CodeLanguage.sql.rawValue,
            code: reply?.solveSql,
            codeTemplate: nil,
            samples: [],
            executionTimeLimit: nil,
            executionMemoryLimit: nil,
            stepText: step.block.text
        )
    }
}
