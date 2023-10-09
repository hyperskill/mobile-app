import shared
import SwiftUI

final class StepQuizCodeAssembly: StepQuizChildQuizAssembly {
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

    func makeModule() -> StepQuizCodeView {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent

        let viewModel = StepQuizCodeViewModel(
            step: step,
            dataset: dataset,
            reply: reply,
            viewDataMapper: StepQuizCodeViewDataMapper(
                formatter: Formatter(resourceProvider: commonComponent.resourceProvider),
                resourceProvider: commonComponent.resourceProvider
            ),
            provideModuleInputCallback: provideModuleInputCallback
        )

        moduleInput = viewModel
        viewModel.moduleOutput = moduleOutput

        return StepQuizCodeView(viewModel: viewModel)
    }
}

#if DEBUG
extension StepQuizCodeAssembly {
    static func makePlaceholder() -> StepQuizCodeAssembly {
        let blockOptions = Block.Options(
            limits: [
                "kotlin": .init(time: 8, memory: 256)
            ],
            codeTemplates: [
                "kotlin": "fun main() {\n    // put your code here\n}"
            ],
            samples: [
                [
                    "3\n3\n3\n",
                    "true"
                ],
                [
                    "40\n30\n50\n",
                    "true"
                ],
                [
                    "40\n100\n20\n",
                    "true"
                ]
            ]
        )
        let step = Step(
            block: Block(
                options: blockOptions
            )
        )

        return StepQuizCodeAssembly(
            step: step,
            dataset: .init(),
            reply: nil,
            provideModuleInputCallback: { _ in },
            moduleOutput: nil
        )
    }
}
#endif
