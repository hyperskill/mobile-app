import Foundation
import shared

struct StepQuizMatchingViewData {
    var items: [MatchItem]

    struct MatchItem: Hashable {
        static func == (lhs: StepQuizMatchingViewData.MatchItem, rhs: StepQuizMatchingViewData.MatchItem) -> Bool {
            lhs.title.id == rhs.title.id && lhs.option.id == rhs.option.id
        }

        func hash(into hasher: inout Hasher) {
            hasher.combine(title.id)
            hasher.combine(option.id)
        }

        let title: Title
        var option: Option

        struct Title {
            let id: Int
            let text: String
        }

        struct Option {
            let id: Int
            let text: String
        }
    }
}

#if DEBUG
extension StepQuizMatchingViewData {
    static func makePlaceholder() -> StepQuizMatchingViewData {
        StepQuizMatchingViewData(
            items: [
                MatchItem(title: .init(id: 0, text: "a"), option: .init(id: 0, text: "Variant 1")),
                MatchItem(title: .init(id: 1, text: "b"), option: .init(id: 1, text: "Variant 2")),
                MatchItem(title: .init(id: 2, text: "c"), option: .init(id: 2, text: "Variant 3")),
                MatchItem(title: .init(id: 3, text: "d"), option: .init(id: 3, text: "Variant 4"))
            ]
        )
    }
}
#endif
