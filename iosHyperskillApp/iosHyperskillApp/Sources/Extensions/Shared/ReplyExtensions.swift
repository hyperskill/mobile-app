import Foundation
import shared

extension Reply {
    convenience init(
        sortingChoices: [Bool]? = nil,
        tableChoices: [TableChoiceAnswer]? = nil,
        text: String? = nil,
        attachments: [Attachment]? = nil,
        formula: String? = nil,
        number: String? = nil,
        ordering: [Int]? = nil,
        language: String? = nil,
        code: String? = nil,
        blanks: [String]? = nil,
        solveSql: String? = nil
    ) {
        let choicesAnswer: [ChoiceAnswer]? = {
            if let sortingChoices = sortingChoices {
                return sortingChoices.map(ChoiceAnswerChoice.init(boolValue:))
            } else if let tableChoices = tableChoices {
                return tableChoices.map(ChoiceAnswerTable.init(tableChoice:))
            }
            return nil
        }()

        self.init(
            choices: choicesAnswer,
            text: text,
            attachments: attachments,
            formula: formula,
            number: number,
            ordering: ordering?.map({ KotlinInt(value: Int32($0)) }),
            language: language,
            code: code,
            blanks: blanks,
            solveSql: solveSql
        )
    }
}
