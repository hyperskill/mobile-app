import Foundation

struct StepQuizMatchingViewData {
    var items: [MatchItem]

    struct MatchItem: Hashable, Identifiable {
        var id: Int { title.id }

        let title: Title
        var option: Option?

        struct Title: Hashable, Identifiable {
            let id: Int
            let text: String
        }

        struct Option: Hashable {
            let id: Int
            let text: String
        }
    }
}
