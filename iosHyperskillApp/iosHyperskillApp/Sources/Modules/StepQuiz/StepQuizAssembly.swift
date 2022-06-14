import shared
import SwiftUI

final class StepQuizAssembly: Assembly {
    private let step: Step

    init(step: Step) {
        self.step = step
    }

    func makeModule() -> StepQuizView {
        let stepQuizComponent = AppGraphBridge.sharedAppGraph.buildStepQuizComponent()

        let viewModel = StepQuizViewModel(
            step: self.step,
            viewDataMapper: StepQuizViewDataMapper(stepQuizStatsTextMapper: stepQuizComponent.stepQuizStatsTextMapper),
            feature: stepQuizComponent.stepQuizFeature
        )

        return StepQuizView(viewModel: viewModel)
    }
}
