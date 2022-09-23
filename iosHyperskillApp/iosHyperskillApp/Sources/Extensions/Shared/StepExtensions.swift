import Foundation
import shared

extension Step {
    convenience init(
        id: Int = 0,
        stepikID: Int = 0,
        lessonStepikID: Int = 0,
        position: Int = 0,
        title: String = "",
        type: Type_ = Type_.theory,
        block: Block = .init(),
        topic: Int = 0,
        topicTheory: Int = 0,
        canAbandon: Bool = false,
        canSkip: Bool = false,
        commentsStatistics: [CommentStatisticsEntry] = [],
        contentCreatedAt: Kotlinx_datetimeInstant = .fromSwiftCurrentDate(),
        contentUpdatedAt: Kotlinx_datetimeInstant = .fromSwiftCurrentDate(),
        solvedBy: Int = 0,
        isCompleted: Bool = false,
        secondsToComplete: Float? = nil,
        lastCompletedAt: Kotlinx_datetimeInstant = .fromSwiftCurrentDate()
    ) {
        self.init(
            id: Int64(id),
            stepikId: Int64(stepikID),
            lessonStepikId: Int64(lessonStepikID),
            position: Int32(position),
            title: title,
            type: type,
            block: block,
            topic: KotlinLong(value: Int64(topic)),
            topicTheory: KotlinLong(value: Int64(topicTheory)),
            canAbandon: canAbandon,
            canSkip: canSkip,
            commentsStatistics: commentsStatistics,
            contentCreatedAt: contentCreatedAt,
            contentUpdatedAt: contentUpdatedAt,
            solvedBy: Int32(solvedBy),
            isCompleted: isCompleted,
            secondsToComplete: secondsToComplete != nil ? KotlinFloat(value: secondsToComplete.require()) : nil,
            lastCompletedAt: lastCompletedAt
        )
    }
}
