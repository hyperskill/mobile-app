import shared
import SwiftUI

final class StepQuizCodeFullScreenAssembly: Assembly {
    private weak var moduleOutput: StepQuizCodeFullScreenOutputProtocol?

    private let codeQuizViewData: StepQuizCodeViewData
    private let initialTab: StepQuizCodeFullScreenTab

    init(
        codeQuizViewData: StepQuizCodeViewData,
        initialTab: StepQuizCodeFullScreenTab = .code,
        output: StepQuizCodeFullScreenOutputProtocol? = nil
    ) {
        self.codeQuizViewData = codeQuizViewData
        self.initialTab = initialTab
        self.moduleOutput = output
    }

    func makeModule() -> StepQuizCodeFullScreenView {
        let viewModel = StepQuizCodeFullScreenViewModel(codeQuizViewData: codeQuizViewData)
        viewModel.moduleOutput = moduleOutput

        return StepQuizCodeFullScreenView(viewModel: viewModel, initialTab: initialTab)
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
                executionTimeLimit: "Time limit: 8 seconds",
                executionMemoryLimit: "Memory limit: 256 MB",
                stepText: """
Enter only the name of the found functional interface with/without the package. Don't write any generic parameters.
""",
                stepStats: "2438 users solved this problem. Latest completion was about 13 hours ago."
            )
        )
    }
}
#endif
