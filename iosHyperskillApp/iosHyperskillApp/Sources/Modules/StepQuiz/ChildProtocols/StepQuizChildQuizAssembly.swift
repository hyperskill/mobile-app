import Foundation
import shared
import SwiftUI

protocol StepQuizChildQuizAssembly: Assembly {
    var moduleInput: StepQuizChildQuizInputProtocol? { get }
    var moduleOutput: StepQuizChildQuizOutputProtocol? { get set }

    init(
        step: Step,
        dataset: Dataset,
        reply: Reply?,
        provideModuleInputCallback: @escaping (StepQuizChildQuizInputProtocol?) -> Void,
        moduleOutput: StepQuizChildQuizOutputProtocol?
    )
}

enum StepQuizChildQuizViewFactory {
    @ViewBuilder
    static func make(
        quizType: StepQuizChildQuizType,
        step: Step,
        dataset: Dataset,
        reply: Reply?,
        provideModuleInputCallback: @escaping (StepQuizChildQuizInputProtocol?) -> Void,
        moduleOutput: StepQuizChildQuizOutputProtocol?
    ) -> some View {
        switch quizType {
        case .choice:
            StepQuizChoiceAssembly(
                step: step,
                dataset: dataset,
                reply: reply,
                provideModuleInputCallback: provideModuleInputCallback,
                moduleOutput: moduleOutput
            )
            .makeModule()
        case .code:
            StepQuizCodeAssembly(
                step: step,
                dataset: dataset,
                reply: reply,
                provideModuleInputCallback: provideModuleInputCallback,
                moduleOutput: moduleOutput
            )
            .makeModule()
        case .sql:
            StepQuizSQLAssembly(
                step: step,
                dataset: dataset,
                reply: reply,
                provideModuleInputCallback: provideModuleInputCallback,
                moduleOutput: moduleOutput
            )
            .makeModule()
        case .pycharm:
            StepQuizPyCharmAssembly(
                step: step,
                dataset: dataset,
                reply: reply,
                provideModuleInputCallback: provideModuleInputCallback,
                moduleOutput: moduleOutput
            )
            .makeModule()
        case .matching:
            StepQuizMatchingAssembly(
                step: step,
                dataset: dataset,
                reply: reply,
                provideModuleInputCallback: provideModuleInputCallback,
                moduleOutput: moduleOutput
            )
            .makeModule()
        case .sorting:
            StepQuizSortingAssembly(
                step: step,
                dataset: dataset,
                reply: reply,
                provideModuleInputCallback: provideModuleInputCallback,
                moduleOutput: moduleOutput
            )
            .makeModule()
        case .table:
            StepQuizTableAssembly(
                step: step,
                dataset: dataset,
                reply: reply,
                provideModuleInputCallback: provideModuleInputCallback,
                moduleOutput: moduleOutput
            )
            .makeModule()
        case .string, .number, .math:
            StepQuizStringAssembly(
                dataType: .init(quizType: quizType).require(),
                step: step,
                dataset: dataset,
                reply: reply,
                provideModuleInputCallback: provideModuleInputCallback,
                moduleOutput: moduleOutput
            )
            .makeModule()
        case .unsupported(let blockName):
            fatalError("Unsupported quiz = \(blockName)")
        }
    }
}
