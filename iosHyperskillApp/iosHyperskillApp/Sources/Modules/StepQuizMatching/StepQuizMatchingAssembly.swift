import shared
import SwiftUI

final class StepQuizMatchingAssembly: StepQuizChildQuizAssembly {
    var moduleInput: StepQuizChildQuizInputProtocol? {
        didSet {
            onModuleInputDidSet(moduleInput)
        }
    }
    weak var moduleOutput: StepQuizChildQuizOutputProtocol?

    private let step: Step
    private let dataset: Dataset
    private let reply: Reply?

    private let onModuleInputDidSet: (StepQuizChildQuizInputProtocol?) -> Void

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

    func makeModule() -> StepQuizMatchingView {
        let viewModel = StepQuizMatchingViewModel(dataset: dataset, reply: reply)

        moduleInput = viewModel
        viewModel.moduleOutput = moduleOutput

        return StepQuizMatchingView(viewModel: viewModel)
    }
}

#if DEBUG
extension StepQuizMatchingAssembly {
    static func makePlaceholder() -> StepQuizMatchingAssembly {
        let dataset = Dataset(
            pairs: [
                .init(first: "&#x27;\\n&#x27;", second: "space character"),
                .init(first: "&#x27;\\t&#x27;", second: "backslash character itself"),
                .init(first: "&#x27; &#x27;", second: "new line character"),
                .init(first: "&#x27;\\\\&#x27;", second: "tab character")
            ]
        )
        return StepQuizMatchingAssembly(
            step: .init(),
            dataset: dataset,
            reply: nil,
            onModuleInputDidSet: { _ in },
            moduleOutput: nil
        )
    }
}
#endif
