import Foundation

struct StepQuizTableViewData {
    var rows: [Row]
    let columns: [Column]
    let isMultipleChoice: Bool

    struct Row: Identifiable, Equatable {
        let text: String
        var answers: [Column]

        var id: Int { self.text.hashValue }
    }

    struct Column: Identifiable, Equatable {
        let text: String

        var id: Int { self.text.hashValue }
    }
}

#if DEBUG
extension StepQuizTableViewData {
    static var singleChoicePlaceholder: StepQuizTableViewData {
        StepQuizTableViewData(
            rows: [
                .init(text: "Variant A", answers: [.init(text: "Answer 1")]),
                .init(text: "Variant B", answers: []),
                .init(text: "Variant C", answers: [])
            ],
            columns: [
                .init(text: "Answer 1"),
                .init(text: "Answer 2"),
                .init(text: "Answer 3")
            ],
            isMultipleChoice: false
        )
    }

    static var multipleChoicePlaceholder: StepQuizTableViewData {
        StepQuizTableViewData(
            rows: [
                .init(text: "Variant A", answers: [.init(text: "Answer 1")]),
                .init(text: "Variant B", answers: []),
                .init(text: "Variant C", answers: [])
            ],
            columns: [
                .init(text: "Answer 1"),
                .init(text: "Answer 2"),
                .init(text: "Answer 3")
            ],
            isMultipleChoice: true
        )
    }
}
#endif
