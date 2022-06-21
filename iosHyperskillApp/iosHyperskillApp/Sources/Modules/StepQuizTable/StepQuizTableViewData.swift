import Foundation

struct StepQuizTableViewData {
    var rows: [Row]
    let columns: [Column]
    let isMultipleChoice: Bool

    struct Row: Identifiable {
        let text: String
        var answers: [Column]

        var id: Int { self.text.hashValue }

        var subtitle: String {
            self.answers.map(\.text).joined(separator: ", ")
        }
    }

    struct Column: Identifiable {
        let text: String

        var id: Int { self.text.hashValue }
    }
}
