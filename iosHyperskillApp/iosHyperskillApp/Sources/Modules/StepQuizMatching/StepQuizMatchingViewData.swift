import Foundation

struct StepQuizMatchingViewData {
    var items: [MatchItem]

    struct MatchItem {
        let title: Title
        var option: Option

        struct Title {
            let id: Int
            let text: String
        }

        struct Option: Hashable {
            let id: Int
            let text: String
        }
    }
}
