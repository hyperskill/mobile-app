import Foundation
import shared

final class StepQuizViewDataMapper {
    private let stepQuizStatsTextMapper: StepQuizStatsTextMapper

    private let stepQuizTitleMapper: StepQuizTitleMapper

    init(stepQuizStatsTextMapper: StepQuizStatsTextMapper, stepQuizTitleMapper: StepQuizTitleMapper) {
        self.stepQuizStatsTextMapper = stepQuizStatsTextMapper
        self.stepQuizTitleMapper = stepQuizTitleMapper
    }

    func mapStepToViewData(
        _ step: Step,
        attempt: Attempt?,
        submissionState: StepQuizFeatureSubmissionState?
    ) -> StepQuizViewData {
        let formattedStats = stepQuizStatsTextMapper.getFormattedStepQuizStats(
            users: step.solvedBy,
            millisSinceLastCompleted: step.millisSinceLastCompleted
        )

        let quizName: String? = {
            guard let dataset = attempt?.dataset else {
                return nil
            }

            // Custom title rendering by code quiz
            if step.block.name == BlockName.shared.CODE {
                return nil
            }

            return self.stepQuizTitleMapper.getStepQuizTitle(
                blockName: step.block.name,
                isMultipleChoice: KotlinBoolean(bool: dataset.isMultipleChoice),
                isCheckbox: KotlinBoolean(bool: dataset.isCheckbox)
            )
        }()

        let hintTextOrNil: String? = {
            guard let submissionStateLoaded = submissionState as? StepQuizFeatureSubmissionStateLoaded,
                  let hintText = submissionStateLoaded.submission.hint
            else {
                return nil
            }
            return hintText.isEmpty ? nil : hintText
        }()

        return StepQuizViewData(
            formattedStats: formattedStats,
            stepText: step.block.text,
            stepBlockName: step.block.name,
            quizName: quizName,
            hintText: hintTextOrNil
        )
    }
}
