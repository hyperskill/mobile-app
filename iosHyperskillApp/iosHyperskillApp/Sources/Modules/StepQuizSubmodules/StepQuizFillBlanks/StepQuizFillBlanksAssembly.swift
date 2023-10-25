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
        // It's safe to call require() here, because we are resolving quiz type in StepQuizViewDataMapper
        let mode = (try? FillBlanksResolver.shared.resolve(dataset: dataset)).require()

        let viewModel = StepQuizFillBlanksViewModel(
            step: step,
            dataset: dataset,
            reply: reply,
            mode: mode.wrapped.require(),
            viewDataMapper: StepQuizFillBlanksViewDataMapper(
                fillBlanksItemMapper: FillBlanksItemMapper(mode: mode),
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
