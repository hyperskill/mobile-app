import Foundation
import shared

// MARK: Step

struct StepViewData {
    let title: String

    let formattedType: String
    let formattedTimeToComplete: String

    let text: String

    let commentsStatistics: [StepCommentStatisticViewData]
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
            commentsStatistics: StepCommentStatisticViewData.placeholders
        )
    }
}
#endif

// MARK: - CommentsStatistics -

struct StepCommentStatisticViewData: Identifiable {
    let id: String
    let title: String
}

extension StepCommentStatisticViewData {
    init(commentStatisticsEntry: CommentStatisticsEntry) {
        self.id = commentStatisticsEntry.thread.name
        self.title = CommentThreadTitleMapper(
            resourceProvider: ResourceProviderImpl()
        ).getFormattedStepCommentThreadStatistics(
            thread: commentStatisticsEntry.thread,
            count: commentStatisticsEntry.totalCount
        )
    }
}

extension CommentStatisticsEntry {
    var viewData: StepCommentStatisticViewData {
        StepCommentStatisticViewData(commentStatisticsEntry: self)
    }
}

#if DEBUG
extension StepCommentStatisticViewData {
    static var placeholders: [StepCommentStatisticViewData] {
        [
            .init(id: "comment", title: "Comments (36)"),
            .init(id: "hint", title: "Comments (0)"),
            .init(id: "useful link", title: "Useful links (1)"),
            .init(id: "solutions", title: "Solutions (0)")
        ]
    }
}
#endif
