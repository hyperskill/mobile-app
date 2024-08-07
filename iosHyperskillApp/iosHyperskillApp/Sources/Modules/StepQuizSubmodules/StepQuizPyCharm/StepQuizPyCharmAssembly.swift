import shared
import SwiftUI

final class StepQuizPyCharmAssembly: StepQuizChildQuizAssembly {
    var moduleInput: StepQuizChildQuizInputProtocol?
    weak var moduleOutput: StepQuizChildQuizOutputProtocol?

    private let provideModuleInputCallback: (StepQuizChildQuizInputProtocol?) -> Void

    private let step: Step
    private let dataset: Dataset
    private let reply: Reply?

    init(
        step: Step,
        dataset: Dataset,
        reply: Reply?,
        provideModuleInputCallback: @escaping (StepQuizChildQuizInputProtocol?) -> Void,
        moduleOutput: StepQuizChildQuizOutputProtocol?
    ) {
        self.step = step
        self.dataset = dataset
        self.reply = reply
        self.provideModuleInputCallback = provideModuleInputCallback
        self.moduleOutput = moduleOutput
    }

    func makeModule() -> StepQuizPyCharmView {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent

        let viewModel = StepQuizPyCharmViewModel(
            step: step,
            dataset: dataset,
            reply: reply,
            viewDataMapper: StepQuizPyCharmViewDataMapper(
                formatter: Formatter(resourceProvider: commonComponent.resourceProvider),
                resourceProvider: commonComponent.resourceProvider
            ),
            provideModuleInputCallback: provideModuleInputCallback
        )

        moduleInput = viewModel
        viewModel.moduleOutput = moduleOutput

        return StepQuizPyCharmView(viewModel: viewModel)
    }
}
