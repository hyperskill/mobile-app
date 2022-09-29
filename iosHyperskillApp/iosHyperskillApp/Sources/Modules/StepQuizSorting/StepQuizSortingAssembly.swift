import shared
import SwiftUI

final class StepQuizSortingAssembly: StepQuizChildQuizAssembly {
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

    func makeModule() -> StepQuizSortingView {
        let viewModel = StepQuizSortingViewModel(dataset: dataset, reply: reply)

        moduleInput = viewModel
        viewModel.moduleOutput = moduleOutput

        return StepQuizSortingView(viewModel: viewModel)
    }
}

#if DEBUG
extension StepQuizSortingAssembly {
    static func makePlaceholder() -> StepQuizSortingAssembly {
        let dataset = Dataset(options: ["Byte", "Short", "Long", "Int"])
        return StepQuizSortingAssembly(
            step: .init(),
            dataset: dataset,
            reply: nil,
            onModuleInputDidSet: { _ in },
            moduleOutput: nil
        )
    }
}
#endif
