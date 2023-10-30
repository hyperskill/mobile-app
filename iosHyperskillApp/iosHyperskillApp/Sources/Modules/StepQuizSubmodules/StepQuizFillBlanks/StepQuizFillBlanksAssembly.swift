import Highlightr
import shared
import SwiftUI

final class StepQuizFillBlanksAssembly: StepQuizChildQuizAssembly {
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

    func makeModule() -> StepQuizFillBlanksView {
        let viewModel = StepQuizFillBlanksViewModel(
            step: step,
            dataset: dataset,
            reply: reply,
            viewDataMapper: StepQuizFillBlanksViewDataMapper(
                fillBlanksItemMapper: FillBlanksItemMapper(mode: .input),
                highlightr: Highlightr().require(),
                codeEditorThemeService: CodeEditorThemeService(),
                cache: StepQuizFillBlanksViewDataMapperCache.shared
            ),
            provideModuleInputCallback: provideModuleInputCallback
        )

        moduleInput = viewModel
        viewModel.moduleOutput = moduleOutput

        return StepQuizFillBlanksView(viewModel: viewModel)
    }
}
