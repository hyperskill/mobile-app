import Foundation
import shared

final class StepQuizViewDataMapper {
    private let stepQuizStatsTextMapper: StepQuizStatsTextMapper

    private let stepQuizTitleMapper: StepQuizTitleMapper

    init(stepQuizStatsTextMapper: StepQuizStatsTextMapper, stepQuizTitleMapper: StepQuizTitleMapper) {
        self.stepQuizStatsTextMapper = stepQuizStatsTextMapper
        self.stepQuizTitleMapper = stepQuizTitleMapper
    }

    func mapStepToViewData(_ step: Step) -> StepQuizViewData {
        let formattedStats = self.stepQuizStatsTextMapper.getFormattedStepQuizStats(
            users: step.solvedBy,
            hours: 1 // TODO: Use `step.last_completed_at`
        )

        return StepQuizViewData(
            formattedStats: formattedStats,
            stepText: step.block.text,
            quizName: self.stepQuizTitleMapper.getStepQuizTitle(block: step.block)
        )
    }
}
