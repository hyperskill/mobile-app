import shared
import SwiftUI

final class ProblemOfDayAssembly: Assembly {
    private weak var moduleOutput: ProblemOfDayOutputProtocol?

    private let problemOfDayState: HomeFeatureProblemOfDayState

    private let isFreemiumEnabled: Bool

    init(
        problemOfDayState: HomeFeatureProblemOfDayState,
        isFreemiumEnabled: Bool,
        output: ProblemOfDayOutputProtocol? = nil
    ) {
        self.problemOfDayState = problemOfDayState
        self.isFreemiumEnabled = isFreemiumEnabled
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

        return ProblemOfDayCardView(viewModel: viewModel, isFreemiumEnabled: isFreemiumEnabled)
    }
}

#if DEBUG
extension ProblemOfDayAssembly {
    static func makePlaceholder(
        state: ProblemOfDayCardView.State,
        secondsToComplete: Float = 25.6,
        nextProblemIn: String = "19 hours 13 minutes",
        needToRefresh: Bool = false
    ) -> ProblemOfDayAssembly {
        let step = Step(secondsToComplete: secondsToComplete)

        switch state {
        case .completed:
            let problemOfDayState = HomeFeatureProblemOfDayStateSolved(
                step: step,
                nextProblemIn: nextProblemIn,
                needToRefresh: needToRefresh
            )
            return ProblemOfDayAssembly(problemOfDayState: problemOfDayState, isFreemiumEnabled: true)
        case .uncompleted:
            let problemOfDayState = HomeFeatureProblemOfDayStateNeedToSolve(
                step: step,
                nextProblemIn: nextProblemIn,
                needToRefresh: needToRefresh
            )
            return ProblemOfDayAssembly(problemOfDayState: problemOfDayState, isFreemiumEnabled: true)
        case .unavailable:
            return ProblemOfDayAssembly(problemOfDayState: HomeFeatureProblemOfDayStateEmpty(), isFreemiumEnabled: true)
        }
    }
}
#endif
