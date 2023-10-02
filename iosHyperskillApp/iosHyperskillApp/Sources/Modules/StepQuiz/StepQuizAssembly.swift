import shared
import SwiftUI

final class StepQuizAssembly: Assembly {
    private let step: Step
    private let stepRoute: StepRoute

    private let provideModuleInputCallback: (StepQuizInputProtocol?) -> Void
    private weak var moduleOutput: StepQuizOutputProtocol?

    init(
        step: Step,
        stepRoute: StepRoute,
        provideModuleInputCallback: @escaping (StepQuizInputProtocol?) -> Void,
        moduleOutput: StepQuizOutputProtocol? = nil
    ) {
        self.step = step
        self.stepRoute = stepRoute
        self.provideModuleInputCallback = provideModuleInputCallback
        self.moduleOutput = moduleOutput
    }

    func makeModule() -> StepQuizView {
        let stepQuizComponent = AppGraphBridge.sharedAppGraph.buildStepQuizComponent(stepRoute: self.stepRoute)

        let viewDataMapper = StepQuizViewDataMapper(
            stepQuizStatsTextMapper: stepQuizComponent.stepQuizStatsTextMapper,
            stepQuizTitleMapper: stepQuizComponent.stepQuizTitleMapper
        )

        let viewModel = StepQuizViewModel(
            step: step,
            stepRoute: stepRoute,
            moduleOutput: moduleOutput,
            provideModuleInputCallback: provideModuleInputCallback,
            viewDataMapper: viewDataMapper,
            feature: stepQuizComponent.stepQuizFeature
        )

        return StepQuizView(viewModel: viewModel)
    }
}
