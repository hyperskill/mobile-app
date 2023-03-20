import Foundation
import shared

final class ProblemsLimitAssembly: Assembly {
    func makeModule() -> ProblemsLimitView {
        let problemsLimitComponent = AppGraphBridge.sharedAppGraph.buildProblemsLimitComponent()

        let viewModel = ProblemsLimitViewModel(
            feature: problemsLimitComponent.problemsLimitFeature
        )

        return ProblemsLimitView(viewModel: viewModel)
    }
}
