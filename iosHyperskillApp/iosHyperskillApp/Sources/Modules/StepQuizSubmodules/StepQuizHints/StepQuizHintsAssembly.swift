import Foundation
import shared

final class StepQuizHintsAssembly: Assembly {
    private let stepID: Int64
    private let stepRoute: StepRoute

    init(stepID: Int64, stepRoute: StepRoute) {
        self.stepID = stepID
        self.stepRoute = stepRoute
    }

    func makeModule() -> StepQuizHintsView {
        let stepQuizHintsComponent = AppGraphBridge.sharedAppGraph.buildStepQuizHintsComponent(
            stepRoute: stepRoute
        )

        let viewModel = StepQuizHintsViewModel(
            stepID: self.stepID,
            feature: stepQuizHintsComponent.stepQuizHintsFeature
        )

        return StepQuizHintsView(viewModel: viewModel)
    }
}
