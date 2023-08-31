import Foundation

struct StepQuizParsonsViewData {
    var lines: [Line]

    var selectedLineNumber: Int?

    struct Line: Hashable {
        let lineNumber: Int
        let text: String
        var level: Int
    }
}
