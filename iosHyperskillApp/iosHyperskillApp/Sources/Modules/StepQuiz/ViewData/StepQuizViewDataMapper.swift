import Foundation
import shared

final class StepQuizViewDataMapper {
    private let stepQuizStatsTextMapper: StepQuizStatsTextMapper
    private let stepQuizTitleMapper: StepQuizTitleMapper
    private let stepQuizFeedbackMapper: StepQuizFeedbackMapper

    init(
        stepQuizStatsTextMapper: StepQuizStatsTextMapper,
        stepQuizTitleMapper: StepQuizTitleMapper,
        stepQuizFeedbackMapper: StepQuizFeedbackMapper
    ) {
        self.stepQuizStatsTextMapper = stepQuizStatsTextMapper
        self.stepQuizTitleMapper = stepQuizTitleMapper
        self.stepQuizFeedbackMapper = stepQuizFeedbackMapper
    }

    func mapStepDataToViewData(
        step: Step,
        stepRoute: StepRoute,
        state: StepQuizFeature.State
    ) -> StepQuizViewData {
        let attemptLoadedState = StepQuizStateExtensionsKt.attemptLoadedState(state.stepQuizState)

        let quizType = resolveQuizType(
            step: step,
            state: state,
            attemptLoadedState: attemptLoadedState
        )

        let navigationTitle = stepRoute is StepRouteStageImplement
            ? nil
            : step.title

        if quizType.isUnsupported || state.stepQuizState is StepQuizFeatureStepQuizStateNetworkError {
            return StepQuizViewData(
                navigationTitle: navigationTitle,
                formattedStats: nil,
                stepTextHeaderTitle: nil,
                stepText: nil,
                quizType: quizType,
                quizName: nil,
                stepQuizFeedbackState: nil
            )
        }

        let stepTextHeaderTitle = stepRoute is StepRouteStageImplement
            ? Strings.StepQuiz.stepTextHeaderTitle
            : step.title

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

        let stepQuizFeedbackState = stepQuizFeedbackMapper.map(state: state)

        return StepQuizViewData(
            navigationTitle: navigationTitle,
            formattedStats: formattedStats,
            stepTextHeaderTitle: stepTextHeaderTitle,
            stepText: step.block.text,
            quizType: quizType,
            quizName: quizName,
            stepQuizFeedbackState: .init(stepQuizFeedbackState)
        )
    }

    private func resolveQuizType(
        step: Step,
        state: StepQuizFeature.State,
        attemptLoadedState: StepQuizFeatureStepQuizStateAttemptLoaded?
    ) -> StepQuizChildQuizType {
        if state.stepQuizState is StepQuizFeatureStepQuizStateUnsupported {
            .unsupported(blockName: step.block.name)
        } else {
            StepQuizChildQuizType.resolve(
                step: step,
                datasetOrNil: attemptLoadedState?.attempt.dataset
            )
        }
    }
}
