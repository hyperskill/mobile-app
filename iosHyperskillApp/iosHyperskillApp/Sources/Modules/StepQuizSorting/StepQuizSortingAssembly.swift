import shared
import SwiftUI

final class StepQuizSortingAssembly: StepQuizChildQuizAssembly {
    weak var delegate: StepQuizChildQuizDelegate?

    private let dataset: Dataset
    private let reply: Reply?

    init(dataset: Dataset, reply: Reply?, delegate: StepQuizChildQuizDelegate?) {
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
        return StepQuizSortingAssembly(dataset: dataset, reply: nil, delegate: nil)
    }
}
#endif
