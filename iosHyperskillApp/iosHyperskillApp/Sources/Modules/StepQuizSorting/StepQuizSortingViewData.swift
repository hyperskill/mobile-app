import Foundation
import shared

struct StepQuizSortingViewData {
    var items: [Option]

    struct Option {
        var id: Int
        var text: String
    }
}

#if DEBUG
extension StepQuizSortingViewData {
    static func makePlaceholder(quizStatus: QuizStatus? = nil) -> StepQuizSortingViewData {
        StepQuizSortingViewData(
            items: [
                Option(id: 0, text: "char[] characters = new char[0];"),
                Option(id: 1, text: "char[] characters = new char[1];"),
                Option(id: 2, text: "char[] characters = new char[555];"),
                Option(id: 3, text: "char[] characters = new char[-5];")
            ]
        )
    }
}
#endif
