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
        let formattedTimeToComplete = self.mapTimeToComplete(seconds: step.secondsToComplete?.floatValue ?? 60)

        let commentsStatistics = step.commentsStatistics.compactMap(self.mapCommentStatisticsEntryToViewData(_:))

        return StepViewData(
            title: step.title,
            formattedTimeToComplete: formattedTimeToComplete,
            text: step.block.text,
            commentsStatistics: commentsStatistics
        )
    }

    // MARK: Private API

    private func mapTimeToComplete(seconds: Float) -> String {
        let minutesQuantityString = dateFormatter.formatMinutesOrSecondsCount(secondsToFormat: seconds)

        return self.resourceProvider.getString(
            stringResource: SharedResources.strings.shared.step_theory_reading_text,
            args: KotlinArray(size: 1, init: { _ in NSString(string: minutesQuantityString) })
        )
    }

    private func mapCommentStatisticsEntryToViewData(
        _ commentStatisticsEntry: CommentStatisticsEntry
    ) -> StepCommentStatisticViewData? {
        guard let thread = commentStatisticsEntry.thread else {
            return nil
        }

        let title = self.commentThreadTitleMapper.getFormattedStepCommentThreadStatistics(
            thread: thread,
            count: commentStatisticsEntry.totalCount
        )

        return StepCommentStatisticViewData(id: thread.name, title: title)
    }
}
