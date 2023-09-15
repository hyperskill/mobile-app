import shared
import SwiftUI

final class StepQuizChoiceAssembly: StepQuizChildQuizAssembly {
    var moduleInput: StepQuizChildQuizInputProtocol? {
        didSet {
            provideModuleInputCallback(moduleInput)
        }
    }
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

    func makeModule() -> StepQuizChoiceView {
        let viewModel = StepQuizChoiceViewModel(dataset: dataset, reply: reply)

        moduleInput = viewModel
        viewModel.moduleOutput = moduleOutput

        return StepQuizChoiceView(viewModel: viewModel)
    }
}

#if DEBUG
extension StepQuizChoiceAssembly {
    static func makePlaceholder(isMultipleChoice: Bool) -> StepQuizChoiceAssembly {
        let dataset = Dataset(
            options: [
                "The more sophisticated the task, the harder to learn language for it.",
                "You need to learn every programming language to be good programmer.",
                "JavaScript is used in a field of front-end development.",
                "It is better to learn programming languages needed for your field of work"
            ],
            isMultipleChoice: isMultipleChoice
        )
        return StepQuizChoiceAssembly(
            step: .init(),
            dataset: dataset,
            reply: nil,
            provideModuleInputCallback: { _ in },
            moduleOutput: nil
        )
    }
}
#endif
