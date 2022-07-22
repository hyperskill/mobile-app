import Foundation
import shared

extension Step {
    convenience init(
        id: Int = 0,
        stepikId: Int = 0,
        lessonStepikId: Int = 0,
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
        secondsToComplete: Float? = nil
    ) {
        self.init(
            id: Int64(id),
            stepikId: Int64(stepikId),
            lessonStepikId: Int64(lessonStepikId),
            position: Int32(position),
            title: title,
            type: type,
            block: block,
            topic: Int64(topic),
            topicTheory: Int64(topicTheory),
            canAbandon: canAbandon,
            canSkip: canSkip,
            commentsStatistics: commentsStatistics,
            contentCreatedAt: contentCreatedAt,
            contentUpdatedAt: contentUpdatedAt,
            solvedBy: Int32(solvedBy),
            secondsToComplete: secondsToComplete != nil ? KotlinFloat(value: secondsToComplete.require()) : nil
        )
    }
}
