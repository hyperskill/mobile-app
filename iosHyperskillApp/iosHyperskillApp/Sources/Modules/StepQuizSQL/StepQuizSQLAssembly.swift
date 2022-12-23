import shared
import SwiftUI

final class StepQuizSQLAssembly: StepQuizChildQuizAssembly {
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

    func makeModule() -> StepQuizSQLView {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent

        let viewModel = StepQuizSQLViewModel(
            step: step,
            dataset: dataset,
            reply: reply,
            viewDataMapper: StepQuizSQLViewDataMapper(
                formatter: Formatter(resourceProvider: commonComponent.resourceProvider),
                resourceProvider: commonComponent.resourceProvider,
                stepQuizStatsTextMapper: StepQuizStatsTextMapper(resourceProvider: commonComponent.resourceProvider)
            ),
            provideModuleInputCallback: provideModuleInputCallback
        )

        moduleInput = viewModel
        viewModel.moduleOutput = moduleOutput

        return StepQuizSQLView(viewModel: viewModel)
    }
}

#if DEBUG
extension StepQuizSQLAssembly {
    static func makePlaceholder() -> StepQuizSQLAssembly {
        StepQuizSQLAssembly(
            step: .init(),
            dataset: .init(),
            reply: .init(solveSql: "DROP TABLE students;"),
            provideModuleInputCallback: { _ in },
            moduleOutput: nil
        )
    }
}
#endif
