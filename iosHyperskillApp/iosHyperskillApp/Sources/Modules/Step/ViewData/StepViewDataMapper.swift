import Foundation
import shared

final class StepViewDataMapper {
    private let resourceProvider: ResourceProvider
    private let commentThreadTitleMapper: CommentThreadTitleMapper

    init(resourceProvider: ResourceProvider, commentThreadTitleMapper: CommentThreadTitleMapper) {
        self.resourceProvider = resourceProvider
        self.commentThreadTitleMapper = commentThreadTitleMapper
    }

    func mapStepToViewData(_ step: Step) -> StepViewData {
        let formattedTimeToComplete = self.mapTimeToComplete(minutes: 1)

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

    private func mapTimeToComplete(minutes: Int32) -> String {
        let minutesQuantityString = self.resourceProvider.getQuantityString(
            pluralsResource: SharedResources.plurals.shared.minutes,
            quantity: minutes,
            args: KotlinArray(size: 1, init: { _ in NSNumber(value: minutes) })
        )

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
