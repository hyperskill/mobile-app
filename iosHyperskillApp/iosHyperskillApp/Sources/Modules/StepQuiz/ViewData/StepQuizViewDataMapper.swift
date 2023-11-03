import Foundation
import shared

final class StepQuizViewDataMapper {
    private let stepQuizStatsTextMapper: StepQuizStatsTextMapper

    private let stepQuizTitleMapper: StepQuizTitleMapper

    init(stepQuizStatsTextMapper: StepQuizStatsTextMapper, stepQuizTitleMapper: StepQuizTitleMapper) {
        self.stepQuizStatsTextMapper = stepQuizStatsTextMapper
        self.stepQuizTitleMapper = stepQuizTitleMapper
    }

    func mapStepDataToViewData(step: Step, state: StepQuizFeatureStepQuizStateKs) -> StepQuizViewData {
        let attemptLoadedState = StepQuizStateExtentionsKt.attemptLoadedState(state.sealed)

        let quizType = resolveQuizType(
            step: step,
            state: state,
            attemptLoadedState: attemptLoadedState
        )

        if case .unsupported = quizType {
            return StepQuizViewData(
                formattedStats: nil,
                stepText: step.block.text,
                quizType: quizType,
                quizName: nil,
                feedbackHintText: nil
            )
        }

        let formattedStats = stepQuizStatsTextMapper.getFormattedStepQuizStats(
            solvedByCount: step.solvedBy,
            millisSinceLastCompleted: step.millisSinceLastCompleted
        )

        let quizName: String? = {
            guard let dataset = attemptLoadedState?.attempt.dataset else {
                return nil
            }

            return stepQuizTitleMapper.getStepQuizTitle(
                blockName: step.block.name,
                isMultipleChoice: KotlinBoolean(bool: dataset.isMultipleChoice),
                isCheckbox: KotlinBoolean(bool: dataset.isCheckbox)
            )
        }()

        let feedbackHintText: String? = {
            guard let submissionStateLoaded =
               attemptLoadedState?.submissionState as? StepQuizFeatureSubmissionStateLoaded else {
                return nil
            }

            if submissionStateLoaded.submission.status == SubmissionStatus.rejected,
               let feedback = submissionStateLoaded.submission.feedback {
                let formattedText = FeedbackKt.formattedText(feedback)
                return formattedText.isEmpty ? nil : formattedText
            } else if let hint = submissionStateLoaded.submission.hint {
                return hint.isEmpty ? nil : hint
            } else {
                return nil
            }
        }()

        return StepQuizViewData(
            formattedStats: formattedStats,
            stepText: step.block.text,
            quizType: quizType,
            quizName: quizName,
            feedbackHintText: feedbackHintText
        )
    }

    private func resolveQuizType(
        step: Step,
        state: StepQuizFeatureStepQuizStateKs,
        attemptLoadedState: StepQuizFeatureStepQuizStateAttemptLoaded?
    ) -> StepQuizChildQuizType {
        if state == .unsupported {
            return .unsupported(blockName: step.block.name)
        }

        return StepQuizChildQuizType.resolve(
            step: step,
            datasetOrNil: attemptLoadedState?.attempt.dataset
        )
    }
}
