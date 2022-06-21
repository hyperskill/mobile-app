import Foundation
import shared

final class StepQuizViewDataMapper {
    private let stepQuizStatsTextMapper: StepQuizStatsTextMapper

    private let stepQuizTitleMapper: StepQuizTitleMapper

    init(stepQuizStatsTextMapper: StepQuizStatsTextMapper, stepQuizTitleMapper: StepQuizTitleMapper) {
        self.stepQuizStatsTextMapper = stepQuizStatsTextMapper
        self.stepQuizTitleMapper = stepQuizTitleMapper
    }

    func mapStepToViewData(_ step: Step, attempt: Attempt?) -> StepQuizViewData {
        let formattedStats = self.stepQuizStatsTextMapper.getFormattedStepQuizStats(
            users: step.solvedBy,
            hours: 1 // TODO: Use `step.last_completed_at`
        )

        let quizName: String? = {
            guard let dataset = attempt?.dataset else {
                return nil
            }

            return self.stepQuizTitleMapper.getStepQuizTitle(
                blockName: step.block.name,
                isMultipleChoice: KotlinBoolean(bool: dataset.isMultipleChoice),
                isCheckbox: KotlinBoolean(bool: dataset.isCheckbox)
            )
        }()

        return StepQuizViewData(
            formattedStats: formattedStats,
            stepText: step.block.text,
            stepBlockName: step.block.name,
            quizName: quizName
        )
    }
}
