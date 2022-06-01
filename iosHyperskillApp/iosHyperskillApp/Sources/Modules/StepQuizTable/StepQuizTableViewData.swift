import Foundation

struct StepQuizTableViewData {
    let rows: [Row]
    let columns: [Column]
    let isMultipleChoice: Bool

    struct Row: Identifiable, Equatable {
        let text: String
        let answers: [Column]

        var id: Int { self.text.hashValue }
    }

    struct Column: Identifiable, Equatable {
        let text: String

        var id: Int { self.text.hashValue }
    }
}

#if DEBUG
extension StepQuizTableViewData {
    static var placeholder: StepQuizTableViewData {
        StepQuizTableViewData(
            rows: [],
            columns: [],
            isMultipleChoice: false
        )
    }
}
#endif
