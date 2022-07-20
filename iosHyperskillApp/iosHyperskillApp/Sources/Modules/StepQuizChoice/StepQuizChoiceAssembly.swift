import shared
import SwiftUI

final class StepQuizChoiceAssembly: StepQuizChildQuizAssembly {
    weak var delegate: StepQuizChildQuizDelegate?

    private let blockOptions: Block.Options
    private let dataset: Dataset
    private let reply: Reply?

    init(blockOptions: Block.Options, dataset: Dataset, reply: Reply?, delegate: StepQuizChildQuizDelegate?) {
        self.blockOptions = blockOptions
        self.dataset = dataset
        self.reply = reply
        self.delegate = delegate
    }

    func makeModule() -> StepQuizChoiceView {
        let viewModel = StepQuizChoiceViewModel(dataset: self.dataset, reply: self.reply)
        viewModel.delegate = self.delegate

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
        return StepQuizChoiceAssembly(blockOptions: .init(), dataset: dataset, reply: nil, delegate: nil)
    }
}
#endif
