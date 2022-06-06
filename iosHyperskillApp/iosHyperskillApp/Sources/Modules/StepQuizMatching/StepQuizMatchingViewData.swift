import Foundation
import shared

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

            static func == (
                lhs: StepQuizMatchingViewData.MatchItem.Option,
                rhs: StepQuizMatchingViewData.MatchItem.Option
            ) -> Bool {
                lhs.id == rhs.id
            }

            func hash(into hasher: inout Hasher) {
                hasher.combine(id)
            }
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
