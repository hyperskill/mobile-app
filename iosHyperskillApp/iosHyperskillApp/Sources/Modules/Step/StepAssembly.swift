import shared
import SwiftUI

final class StepAssembly: Assembly {
    private let stepID: Int

    init(stepID: Int) {
        self.stepID = stepID
    }

    func makeModule() -> StepView {
        let stepComponent = AppGraphBridge.shared.buildStepComponent()
        let viewModel = StepViewModel(stepID: self.stepID, feature: stepComponent.stepFeature)

        return StepView(viewModel: viewModel)
    }
}
