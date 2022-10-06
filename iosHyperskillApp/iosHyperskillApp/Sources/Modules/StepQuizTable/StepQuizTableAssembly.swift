import shared
import SwiftUI

final class StepQuizTableAssembly: StepQuizChildQuizAssembly {
    var moduleInput: StepQuizChildQuizInputProtocol? {
        didSet {
            onModuleInputDidSet(moduleInput)
        }
    }
    weak var moduleOutput: StepQuizChildQuizOutputProtocol?

    private let onModuleInputDidSet: (StepQuizChildQuizInputProtocol?) -> Void

    private let step: Step
    private let dataset: Dataset
    private let reply: Reply?

    init(
        step: Step,
        dataset: Dataset,
        reply: Reply?,
        onModuleInputDidSet: @escaping (StepQuizChildQuizInputProtocol?) -> Void,
        moduleOutput: StepQuizChildQuizOutputProtocol?
    ) {
        self.step = step
        self.dataset = dataset
        self.reply = reply
        self.onModuleInputDidSet = onModuleInputDidSet
        self.moduleOutput = moduleOutput
    }

    func makeModule() -> StepQuizTableView {
        let viewModel = StepQuizTableViewModel(dataset: dataset, reply: reply)

        moduleInput = viewModel
        viewModel.moduleOutput = moduleOutput

        return StepQuizTableView(
            viewModel: viewModel,
            panModalPresenter: PanModalPresenter(sourcelessRouter: SourcelessRouter())
        )
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
        return StepQuizTableAssembly(
            step: .init(),
            dataset: dataset,
            reply: nil,
            onModuleInputDidSet: { _ in },
            moduleOutput: nil
        )
    }
}
#endif
