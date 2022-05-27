import shared
import SwiftUI

final class StepAssembly: Assembly {
    private let stepID: Int

    init(stepID: Int) {
        self.stepID = stepID
    }

    func makeModule() -> StepView {
        let feature = AppGraphBridge.shared.buildStepComponent().stepFeature
        let viewModel = StepViewModel(stepID: self.stepID, feature: feature)

        return StepView(viewModel: viewModel)
    }
}
