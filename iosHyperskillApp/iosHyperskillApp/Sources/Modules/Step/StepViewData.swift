import Foundation
import shared

struct StepViewData {
    let title: String

    let formattedType: String
    let formattedTimeToComplete: String

    let text: String

    let commentsStatistics: [StepCommentStatisticViewData]
}

struct StepCommentStatisticViewData: Identifiable {
    let title: String
    let count: Int

    var id: String { self.title }
}

extension Step {
    var viewData: StepViewData {
        StepViewData(
            title: self.title,
            formattedType: self.type.capitalized,
            formattedTimeToComplete: "3 minutes remaining",
            text: self.block.text,
            commentsStatistics: self.commentsStatistics.map(\.viewData)
        )
    }
}

extension CommentStatisticsEntry {
    var viewData: StepCommentStatisticViewData {
        StepCommentStatisticViewData(title: self.thread, count: Int(self.totalCount))
    }
}

#if DEBUG
extension StepViewData {
    static var placeholder: StepViewData {
        StepViewData(
            title: "Introduction to Kotlin",
            formattedType: "Theory",
            formattedTimeToComplete: "6 minutes remaining",
            text: """
<h5 style=\"text-align: center;\">What is Kotlin?</h5>\n\n<p><strong>Kotlin</strong> is a highly effective modern \
programming language developed by <a target=\"_blank\" href=\"https://www.jetbrains.com/\" \
rel=\"noopener noreferrer nofollow\">JetBrains</a>. It has a very clear and concise syntax, which makes your code easy \
to read.
""",
            commentsStatistics: [
                .init(title: "Comments", count: 36),
                .init(title: "Hints", count: 0),
                .init(title: "Useful links", count: 1),
                .init(title: "Solutions", count: 0)
            ]
        )
    }
}
#endif
