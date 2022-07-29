import shared
import SwiftUI

final class StepQuizMatchingAssembly: StepQuizChildQuizAssembly {
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

    func makeModule() -> StepQuizMatchingView {
        let viewModel = StepQuizMatchingViewModel(dataset: self.dataset, reply: self.reply)
        viewModel.delegate = self.delegate

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
        return StepQuizMatchingAssembly(step: .init(), dataset: dataset, reply: nil, delegate: nil)
    }
}
#endif
