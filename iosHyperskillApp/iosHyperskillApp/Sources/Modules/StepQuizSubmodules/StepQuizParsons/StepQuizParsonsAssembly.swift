import Highlightr
import shared
import SwiftUI

final class StepQuizParsonsAssembly: StepQuizChildQuizAssembly {
    var moduleInput: StepQuizChildQuizInputProtocol?
    weak var moduleOutput: StepQuizChildQuizOutputProtocol?

    private let provideModuleInputCallback: (StepQuizChildQuizInputProtocol?) -> Void

    private let step: Step
    private let dataset: Dataset
    private let reply: Reply?

    init(
        step: Step,
        dataset: Dataset,
        reply: Reply?,
        provideModuleInputCallback: @escaping (StepQuizChildQuizInputProtocol?) -> Void,
        moduleOutput: StepQuizChildQuizOutputProtocol?
    ) {
        self.step = step
        self.dataset = dataset
        self.reply = reply
        self.provideModuleInputCallback = provideModuleInputCallback
        self.moduleOutput = moduleOutput
    }

    func makeModule() -> StepQuizParsonsView {
        let viewModel = StepQuizParsonsViewModel(
            step: step,
            dataset: dataset,
            reply: reply,
            viewDataMapper: StepQuizParsonsViewDataMapper(
                highlightr: Highlightr(),
                codeEditorThemeService: CodeEditorThemeService(),
                codeContentCache: StepQuizParsonsViewDataMapperCodeContentCache.shared
            ),
            provideModuleInputCallback: provideModuleInputCallback
        )

        moduleInput = viewModel
        viewModel.moduleOutput = moduleOutput

        return StepQuizParsonsView(viewModel: viewModel)
    }
}

#if DEBUG
extension StepQuizParsonsAssembly {
    static func makePlaceholder() -> StepQuizParsonsAssembly {
        let dataset = Dataset(
            lines: [
                "print(minimum)",
                "minimum = b",
                "if b &lt; minimum:",
                "minimum = a"
            ]
        )
        return StepQuizParsonsAssembly(
            step: .init(),
            dataset: dataset,
            reply: nil,
            provideModuleInputCallback: { _ in },
            moduleOutput: nil
        )
    }
}
#endif
