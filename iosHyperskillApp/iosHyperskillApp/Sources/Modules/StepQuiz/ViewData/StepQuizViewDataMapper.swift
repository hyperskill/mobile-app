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
            let isMultipleChoice = attempt?.dataset?.isMultipleChoice ?? step.block.options.isMultipleChoice?.boolValue
            let isCheckbox = attempt?.dataset?.isCheckbox ?? step.block.options.isCheckbox?.boolValue

            return self.stepQuizTitleMapper.getStepQuizTitle(
                blockName: step.block.name,
                isMultipleChoice: isMultipleChoice != nil ? KotlinBoolean(bool: isMultipleChoice.require()) : nil,
                isCheckbox: isCheckbox != nil ? KotlinBoolean(bool: isCheckbox.require()) : nil
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
