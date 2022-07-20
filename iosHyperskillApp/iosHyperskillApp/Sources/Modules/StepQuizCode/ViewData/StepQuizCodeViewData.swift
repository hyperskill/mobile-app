import Foundation

struct StepQuizCodeViewData {
    let samples: [Sample]

    let executionTimeLimit: String?
    let executionMemoryLimit: String?

    struct Sample: Hashable {
        let inputTitle: String
        let inputValue: String

        let outputTitle: String
        let outputValue: String
    }
}
