import Foundation
import shared

final class StepQuizViewDataMapper {
    private let stepQuizStatsTextMapper: StepQuizStatsTextMapper

    init(stepQuizStatsTextMapper: StepQuizStatsTextMapper) {
        self.stepQuizStatsTextMapper = stepQuizStatsTextMapper
    }

    func mapStepToViewData(_ step: Step) -> StepQuizViewData {
        let formattedStats = self.stepQuizStatsTextMapper.getFormattedStepQuizStats(
            users: step.solvedBy,
            hours: 1 // TODO: Use `step.last_completed_at`
        )

        return StepQuizViewData(
            formattedStats: formattedStats,
            text: step.block.text
        )
    }
}
