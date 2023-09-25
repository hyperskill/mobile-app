import Foundation

struct StepQuizCodeViewData {
    let language: CodeLanguage?
    let languageStringValue: String?

    var code: String?
    let codeTemplate: String?

    let samples: [Sample]

    let stepText: String

    struct Sample: Hashable {
        let inputTitle: String
        let inputValue: String

        let outputTitle: String
        let outputValue: String
    }
}
