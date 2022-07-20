import shared
import SwiftUI

final class StepQuizSortingAssembly: StepQuizChildQuizAssembly {
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

    func makeModule() -> StepQuizSortingView {
        let viewModel = StepQuizSortingViewModel(dataset: self.dataset, reply: self.reply)
        viewModel.delegate = self.delegate

        return StepQuizSortingView(viewModel: viewModel)
    }
}

#if DEBUG
extension StepQuizSortingAssembly {
    static func makePlaceholder() -> StepQuizSortingAssembly {
        let dataset = Dataset(options: ["Byte", "Short", "Long", "Int"])
        return StepQuizSortingAssembly(blockOptions: .init(), dataset: dataset, reply: nil, delegate: nil)
    }
}
#endif
