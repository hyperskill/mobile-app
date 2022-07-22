import shared
import SwiftUI

final class StepQuizTableAssembly: StepQuizChildQuizAssembly {
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

    func makeModule() -> StepQuizTableView {
        let viewModel = StepQuizTableViewModel(dataset: self.dataset, reply: self.reply)
        viewModel.delegate = self.delegate

        return StepQuizTableView(viewModel: viewModel)
    }
}

#if DEBUG
extension StepQuizTableAssembly {
    static func makePlaceholder(isMultipleChoice: Bool = false) -> StepQuizTableAssembly {
        let dataset = Dataset(
            rows: [
                "incorrect keyword",
                "incorrect text output",
                "missing closing bracket",
                "a program cannot open a file"
            ],
            columns: [
                "compile-time",
                "run-time (\"bugs\")"
            ],
            isCheckbox: isMultipleChoice
        )
        return StepQuizTableAssembly(step: .init(), dataset: dataset, reply: nil, delegate: nil)
    }
}
#endif
