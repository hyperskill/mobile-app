import Foundation

struct StepQuizParsonsViewData: Equatable {
    var lines: [Line]

    var selectedLineNumber: Int?

    struct Line: Hashable {
        let lineNumber: Int
        let code: CodeContent
        var level: Int
    }

    enum CodeContent: Hashable {
        case attributedString(NSAttributedString)
        case htmlText(String)
    }
}
