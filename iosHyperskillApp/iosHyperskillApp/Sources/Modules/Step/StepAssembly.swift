import shared
import SwiftUI

final class StepAssembly: Assembly {
    private let stepID: Int

    init(stepID: Int) {
        self.stepID = stepID
    }

    func makeModule() -> StepView {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent
        let stepComponent = AppGraphBridge.sharedAppGraph.buildStepComponent()

        let viewModel = StepViewModel(
            stepID: self.stepID,
            viewDataMapper: StepViewDataMapper(
                formatter: Formatter(resourceProvider: commonComponent.resourceProvider),
                resourceProvider: commonComponent.resourceProvider,
                commentThreadTitleMapper: stepComponent.commentThreadTitleMapper
            ),
            feature: stepComponent.stepFeature
        )

        return StepView(viewModel: viewModel)
    }
}
