import Foundation
import shared

final class StepViewDataMapper {
    private let formatter: Formatter
    private let resourceProvider: ResourceProvider
    private let commentThreadTitleMapper: CommentThreadTitleMapper

    init(
        formatter: Formatter,
        resourceProvider: ResourceProvider,
        commentThreadTitleMapper: CommentThreadTitleMapper
    ) {
        self.formatter = formatter
        self.resourceProvider = resourceProvider
        self.commentThreadTitleMapper = commentThreadTitleMapper
    }

    func mapStepToViewData(_ step: Step) -> StepViewData {
        let formattedTimeToComplete = self.mapTimeToComplete(seconds: step.secondsToComplete?.doubleValue ?? 60)

        let commentsStatistics = step.commentsStatistics.map(self.mapCommentStatisticsEntryToViewData(_:))

        return StepViewData(
            title: step.title,
            formattedType: step.type.name.capitalized,
            formattedTimeToComplete: formattedTimeToComplete,
            text: step.block.text,
            commentsStatistics: commentsStatistics
        )
    }

    // MARK: Private API

    private func mapTimeToComplete(seconds: Double) -> String {
        let minutesQuantityString = formatter.minutesOrSecondsCount(seconds: seconds)

        return self.resourceProvider.getString(
            stringResource: SharedResources.strings.shared.step_theory_reading_text,
            args: KotlinArray(size: 1, init: { _ in NSString(string: minutesQuantityString) })
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
