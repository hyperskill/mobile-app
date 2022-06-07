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
        }
    }
}

#if DEBUG
extension StepQuizMatchingViewData {
    static var placeholder: StepQuizMatchingViewData {
        let pairs = [
            ("a", "Variant 1"),
            ("b", "Variant 2"),
            ("c", "Variant 3")
        ]

        return StepQuizMatchingViewData(
            items: pairs.enumerated().map { index, pair in
                .init(title: .init(id: index, text: pair.0), option: .init(id: index, text: pair.1))
            }
        )
    }
}
#endif
