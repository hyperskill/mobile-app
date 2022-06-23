import shared
import SwiftUI

final class StepQuizStringAssembly: StepQuizChildQuizAssembly {
    weak var delegate: StepQuizChildQuizDelegate?

    private let dataset: Dataset
    private let reply: Reply?

    private var dataType = StepQuizStringDataType.string

    init(
        dataType: StepQuizStringDataType,
        dataset: Dataset,
        reply: Reply?,
        delegate: StepQuizChildQuizDelegate?
    ) {
        self.dataType = dataType
        self.dataset = dataset
        self.reply = reply
        self.delegate = delegate
    }

    init(dataset: Dataset, reply: Reply?, delegate: StepQuizChildQuizDelegate?) {
        self.dataset = dataset
        self.reply = reply
        self.delegate = delegate
    }

    func makeModule() -> StepQuizStringView {
        let viewModel = StepQuizStringViewModel(dataType: self.dataType, dataset: self.dataset, reply: self.reply)
        viewModel.delegate = self.delegate

        return StepQuizStringView(viewModel: viewModel)
    }
}

#if DEBUG
extension StepQuizStringAssembly {
    static func makePlaceholder(dataType: StepQuizStringDataType) -> StepQuizStringAssembly {
        StepQuizStringAssembly(dataType: dataType, dataset: Dataset(), reply: nil, delegate: nil)
    }
}
#endif
