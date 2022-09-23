import Foundation

struct StepQuizSortingViewData {
    var items: [Option]

    struct Option: Hashable {
        var id: Int
        var text: String
    }
}
