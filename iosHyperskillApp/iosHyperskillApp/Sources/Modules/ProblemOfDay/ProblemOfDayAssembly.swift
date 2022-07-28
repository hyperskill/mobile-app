import shared
import SwiftUI

final class ProblemOfDayAssembly: Assembly {
    private weak var moduleOutput: ProblemOfDayOutputProtocol?

    private let problemOfDayState: HomeFeatureProblemOfDayState

    init(problemOfDayState: HomeFeatureProblemOfDayState, output: ProblemOfDayOutputProtocol? = nil) {
        self.problemOfDayState = problemOfDayState
        self.moduleOutput = output
    }

    func makeModule() -> ProblemOfDayCardView {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent

        let viewModel = ProblemOfDayViewModel(
            problemOfDayState: problemOfDayState,
            viewDataMapper: ProblemOfDayViewDataMapper(
                formatter: Formatter(resourceProvider: commonComponent.resourceProvider)
            )
        )
        viewModel.moduleOutput = moduleOutput

        return ProblemOfDayCardView(viewModel: viewModel)
    }
}

#if DEBUG
extension ProblemOfDayAssembly {
    static func makePlaceholder(
        state: ProblemOfDayCardView.State,
        secondsToComplete: Float = 25.6,
        nextProblemIn: Int64 = 21600
    ) -> ProblemOfDayAssembly {
        let step = Step(
            id: 0,
            stepikId: 0,
            lessonStepikId: 0,
            position: 0,
            title: "",
            type: .practice,
            block: .init(
                name: "",
                text: "",
                options: .init(isMultipleChoice: nil, language: nil, isCheckbox: nil),
                tableOfContents: []
            ),
            topic: 0,
            topicTheory: 0,
            canAbandon: false,
            canSkip: false,
            commentsStatistics: [],
            contentCreatedAt: Kotlinx_datetimeInstant.companion.fromEpochMilliseconds(epochMilliseconds: 0),
            contentUpdatedAt: Kotlinx_datetimeInstant.companion.fromEpochMilliseconds(epochMilliseconds: 0),
            solvedBy: 0,
            isCompleted: false,
            secondsToComplete: KotlinFloat(value: secondsToComplete)
        )

        switch state {
        case .completed:
            let problemOfDayState = HomeFeatureProblemOfDayStateSolved(step: step, nextProblemIn: nextProblemIn)
            return ProblemOfDayAssembly(problemOfDayState: problemOfDayState)
        case .uncompleted:
            let problemOfDayState = HomeFeatureProblemOfDayStateNeedToSolve(step: step, nextProblemIn: nextProblemIn)
            return ProblemOfDayAssembly(problemOfDayState: problemOfDayState)
        case .unavailable:
            return ProblemOfDayAssembly(problemOfDayState: HomeFeatureProblemOfDayStateEmpty())
        }
    }
}
#endif
