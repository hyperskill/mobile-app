import shared
import SwiftUI

final class StepQuizCodeAssembly: StepQuizChildQuizAssembly {
    weak var delegate: StepQuizChildQuizDelegate?

    private let step: Step
    private let dataset: Dataset
    private let reply: Reply?

    init(step: Step, dataset: Dataset, reply: Reply?, delegate: StepQuizChildQuizDelegate?) {
        self.step = step
        self.dataset = dataset
        self.reply = reply
        self.delegate = delegate
    }

    func makeModule() -> StepQuizCodeView {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent

        let viewModel = StepQuizCodeViewModel(
            step: self.step,
            dataset: self.dataset,
            reply: self.reply,
            viewDataMapper: StepQuizCodeViewDataMapper(
                formatter: Formatter(resourceProvider: commonComponent.resourceProvider),
                resourceProvider: commonComponent.resourceProvider,
                stepQuizStatsTextMapper: StepQuizStatsTextMapper(resourceProvider: commonComponent.resourceProvider)
            )
        )
        viewModel.delegate = self.delegate

        return StepQuizCodeView(viewModel: viewModel)
    }
}

#if DEBUG
extension StepQuizCodeAssembly {
    static func makePlaceholder() -> StepQuizCodeAssembly {
        let blockOptions = Block.Options(
            executionTimeLimit: 5,
            executionMemoryLimit: 256,
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

        return StepQuizCodeAssembly(step: step, dataset: .init(), reply: nil, delegate: nil)
    }
}
#endif
