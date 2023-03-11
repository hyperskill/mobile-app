import Foundation
import shared

final class StepQuizViewDataMapper {
    private let stepQuizStatsTextMapper: StepQuizStatsTextMapper

    private let stepQuizTitleMapper: StepQuizTitleMapper

    init(stepQuizStatsTextMapper: StepQuizStatsTextMapper, stepQuizTitleMapper: StepQuizTitleMapper) {
        self.stepQuizStatsTextMapper = stepQuizStatsTextMapper
        self.stepQuizTitleMapper = stepQuizTitleMapper
    }

    func mapStepDataToViewData(step: Step, state: StepQuizFeatureState) -> StepQuizViewData {
        let quizType: StepQuizChildQuizType = {
            if state is StepQuizFeatureStateUnsupported {
                return .unsupported(blockName: step.block.name)
            }
            return StepQuizChildQuizType(step: step)
        }()

        if case .unsupported = quizType {
            return StepQuizViewData(
                formattedStats: nil,
                stepText: step.block.text,
                quizType: quizType,
                quizName: nil,
                feedbackHintText: nil,
                stepHasHints: false
            )
        }

        let formattedStats = stepQuizStatsTextMapper.getFormattedStepQuizStats(
            solvedByUsersCount: step.solvedBy,
            millisSinceLastCompleted: step.millisSinceLastCompleted
        )

        let attemptLoadedState = state as? StepQuizFeatureStateAttemptLoaded

        let quizName: String? = {
            guard let dataset = attemptLoadedState?.attempt.dataset else {
                return nil
            }

            // Custom title rendering by code quiz
            if step.block.name == BlockName.shared.CODE {
                return nil
            }

            return stepQuizTitleMapper.getStepQuizTitle(
                blockName: step.block.name,
                isMultipleChoice: KotlinBoolean(bool: dataset.isMultipleChoice),
                isCheckbox: KotlinBoolean(bool: dataset.isCheckbox)
            )
        }()

        let feedbackHintText: String? = {
            guard let submissionLoaded = attemptLoadedState?.submissionState as? StepQuizFeatureSubmissionStateLoaded,
                  let hint = submissionLoaded.submission.hint else {
                return nil
            }
            return hint.isEmpty ? nil : hint
        }()

        let stepHasHints: Bool = {
            guard let hintsStatistic = step.commentsStatistics.first(where: { $0.thread == CommentThread.hint }) else {
                return false
            }
            return hintsStatistic.totalCount > 0
        }()

        return StepQuizViewData(
            formattedStats: formattedStats,
            stepText: step.block.text,
            quizType: quizType,
            quizName: quizName,
            feedbackHintText: feedbackHintText,
            stepHasHints: stepHasHints
        )
    }
}
