import Foundation
import shared

final class StepViewDataMapper {
    private let dateFormatter: SharedDateFormatter
    private let resourceProvider: ResourceProvider
    private let commentThreadTitleMapper: CommentThreadTitleMapper

    init(
        dateFormatter: SharedDateFormatter,
        resourceProvider: ResourceProvider,
        commentThreadTitleMapper: CommentThreadTitleMapper
    ) {
        self.dateFormatter = dateFormatter
        self.resourceProvider = resourceProvider
        self.commentThreadTitleMapper = commentThreadTitleMapper
    }

    func mapStepToViewData(_ step: Step) -> StepViewData {
        let formattedTimeToComplete = dateFormatter.formatMinutesOrSecondsCount(
            secondsToFormat: step.secondsToComplete?.floatValue ?? 60
        )

        let commentsStatistics = step.commentsStatistics.map(self.mapCommentStatisticsEntryToViewData(_:))

        return StepViewData(
            title: step.title,
            formattedTimeToComplete: formattedTimeToComplete,
            text: step.block.text,
            commentsStatistics: commentsStatistics
        )
    }

    private func mapCommentStatisticsEntryToViewData(
        _ commentStatisticsEntry: CommentStatisticsEntry
    ) -> StepCommentStatisticViewData {
        let title = self.commentThreadTitleMapper.getFormattedStepCommentThreadStatistics(
            thread: commentStatisticsEntry.thread,
            count: commentStatisticsEntry.totalCount
        )

        return StepCommentStatisticViewData(id: commentStatisticsEntry.thread.name, title: title)
    }
}
