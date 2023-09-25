import shared
import SwiftUI

final class StepQuizCodeFullScreenAssembly: Assembly {
    var moduleInput: StepQuizCodeFullScreenInputProtocol?
    private weak var moduleOutput: StepQuizCodeFullScreenOutputProtocol?

    private let provideModuleInputCallback: (StepQuizCodeFullScreenInputProtocol?) -> Void

    private let codeQuizViewData: StepQuizCodeViewData
    private let initialTab: StepQuizCodeFullScreenTab

    private let navigationTitle: String

    init(
        codeQuizViewData: StepQuizCodeViewData,
        initialTab: StepQuizCodeFullScreenTab = .code,
        navigationTitle: String = Strings.StepQuizCode.title,
        provideModuleInputCallback: @escaping (StepQuizCodeFullScreenInputProtocol?) -> Void,
        output: StepQuizCodeFullScreenOutputProtocol? = nil
    ) {
        self.codeQuizViewData = codeQuizViewData
        self.initialTab = initialTab
        self.navigationTitle = navigationTitle
        self.provideModuleInputCallback = provideModuleInputCallback
        self.moduleOutput = output
    }

    func makeModule() -> StepQuizCodeFullScreenView {
        let viewModel = StepQuizCodeFullScreenViewModel(
            codeQuizViewData: codeQuizViewData,
            provideModuleInputCallback: provideModuleInputCallback
        )

        viewModel.moduleOutput = moduleOutput
        moduleInput = viewModel

        return StepQuizCodeFullScreenView(
            viewModel: viewModel,
            selectedTab: initialTab,
            navigationTitle: navigationTitle
        )
    }
}

#if DEBUG
extension StepQuizCodeFullScreenAssembly {
    static func makePlaceholder() -> StepQuizCodeFullScreenAssembly {
        StepQuizCodeFullScreenAssembly(
            codeQuizViewData: StepQuizCodeViewData(
                language: .kotlin,
                languageStringValue: "kotlin",
                code: "fun main() {\n    // put your code here\n}",
                codeTemplate: "fun main() {\n    // put your code here\n}",
                samples: [
                    .init(
                        inputTitle: "Sample Input 1",
                        inputValue: "3\n3\n3",
                        outputTitle: "Sample Output 1",
                        outputValue: "true"
                    )
                ],
                stepText: """
Enter only the name of the found functional interface with/without the package. Don't write any generic parameters.
"""
            ),
            provideModuleInputCallback: { _ in }
        )
    }
}
#endif
