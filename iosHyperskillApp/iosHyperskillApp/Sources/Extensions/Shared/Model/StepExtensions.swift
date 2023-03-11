import Foundation
import shared

extension Step {
    convenience init(
        id: Int = 0,
        title: String = "",
        type: Type_ = Type_.theory,
        block: Block = .init(),
        topicTheory: Int = 0,
        topic: Int = 0,
        commentsStatistics: [CommentStatisticsEntry] = [],
        solvedBy: Int = 0,
        isCompleted: Bool = false,
        isNext: Bool = false,
        canSkip: Bool = true,
        checkProfile: String = "",
        secondsToComplete: Float? = nil,
        lastCompletedAt: Kotlinx_datetimeInstant = .fromSwiftCurrentDate()
    ) {
        self.init(
            id: Int64(id),
            title: title,
            type: type,
            block: block,
            topicTheory: KotlinLong(value: Int64(topicTheory)),
            topic: KotlinLong(integerLiteral: topic),
            commentsStatistics: commentsStatistics,
            solvedBy: Int32(solvedBy),
            isCompleted: isCompleted,
            isNext: isNext,
            canSkip: canSkip,
            checkProfile: checkProfile,
            secondsToComplete: secondsToComplete != nil ? KotlinFloat(value: secondsToComplete.require()) : nil,
            lastCompletedAt: lastCompletedAt
        )
    }
}
