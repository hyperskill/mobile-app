import Foundation
import shared

final class StepQuizHintsAssembly: Assembly {
    private let stepID: Int64

    init(stepID: Int64) {
        self.stepID = stepID
    }

    func makeModule() -> StepQuizHintsView {
        let stepQuizHintsComponent = AppGraphBridge.sharedAppGraph.buildStepQuizHintsComponent()

        let viewModel = StepQuizHintsViewModel(
            stepID: self.stepID,
            feature: stepQuizHintsComponent.stepQuizHintsFeature
        )

        return StepQuizHintsView(viewModel: viewModel)
    }
}
