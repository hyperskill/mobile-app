import Foundation
import shared

final class StepQuizSQLViewDataMapper: StepQuizCodeViewDataMapper {
    override func mapCodeDataToViewData(step: Step, reply: Reply?) -> StepQuizCodeViewData {
        StepQuizCodeViewData(
            language: .sql,
            languageStringValue: CodeLanguage.sql.rawValue,
            languageHumanReadableName: CodeLanguage.sql.humanReadableName,
            code: reply?.solveSql,
            codeTemplate: nil,
            samples: [],
            stepText: step.block.text
        )
    }
}
