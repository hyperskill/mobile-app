import Foundation
import shared

struct StepQuizSortingViewData {
    var items: [Item]

    struct Item {
        var id: Int
        var text: String
    }
}

#if DEBUG
extension StepQuizSortingViewData {
    static func makePlaceholder(quizStatus: QuizStatus? = nil) -> StepQuizSortingViewData {
        StepQuizSortingViewData(
            items: [
                Item(id: 0, text: "char[] characters = new char[0];"),
                Item(id: 1, text: "char[] characters = new char[1];"),
                Item(id: 2, text: "char[] characters = new char[555];"),
                Item(id: 3, text: "char[] characters = new char[-5];")
            ]
        )
    }
}
#endif
