import shared
import SwiftUI

final class StepQuizStringAssembly: StepQuizChildQuizAssembly {
    weak var delegate: StepQuizChildQuizDelegate?

    private let step: Step
    private let dataset: Dataset
    private let reply: Reply?

    private var dataType = StepQuizStringDataType.string

    init(
        dataType: StepQuizStringDataType,
        step: Step,
        dataset: Dataset,
        reply: Reply?,
        delegate: StepQuizChildQuizDelegate?
    ) {
        self.dataType = dataType
        self.step = step
        self.dataset = dataset
        self.reply = reply
        self.delegate = delegate
    }

    init(step: Step, dataset: Dataset, reply: Reply?, delegate: StepQuizChildQuizDelegate?) {
        self.step = step
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
        StepQuizStringAssembly(dataType: dataType, step: .init(), dataset: .init(), reply: nil, delegate: nil)
    }
}
#endif
