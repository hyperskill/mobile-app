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

    private let mode: FillBlanksModeWrapper

    init(
        mode: FillBlanksModeWrapper,
        step: Step,
        dataset: Dataset,
        reply: Reply?,
        provideModuleInputCallback: @escaping (StepQuizChildQuizInputProtocol?) -> Void,
        moduleOutput: StepQuizChildQuizOutputProtocol?
    ) {
        self.mode = mode
        self.step = step
        self.dataset = dataset
        self.reply = reply
        self.provideModuleInputCallback = provideModuleInputCallback
        self.moduleOutput = moduleOutput
    }

    // swiftlint:disable:next unavailable_function
    init(
        step: Step,
        dataset: Dataset,
        reply: Reply?,
        provideModuleInputCallback: @escaping (StepQuizChildQuizInputProtocol?) -> Void,
        moduleOutput: StepQuizChildQuizOutputProtocol?
    ) {
        fatalError("init(step:dataset:reply:provideModuleInputCallback:moduleOutput:) has not been implemented")
    }

    func makeModule() -> StepQuizFillBlanksView {
        let viewModel = StepQuizFillBlanksViewModel(
            step: step,
            dataset: dataset,
            reply: reply,
            mode: mode,
            viewDataMapper: StepQuizFillBlanksViewDataMapper(
                fillBlanksItemMapper: FillBlanksItemMapper(mode: mode.sharedValue),
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
