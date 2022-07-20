import Foundation

struct StepQuizCodeViewData {
    let samples: [Sample]

    struct Sample: Hashable {
        let inputTitle: String
        let inputValue: String

        let outputTitle: String
        let outputValue: String
    }
}
