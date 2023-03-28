import Foundation
import shared

final class ProblemsLimitAssembly: Assembly {
    private let showDivider: Bool

    init(showDivider: Bool) {
        self.showDivider = showDivider
    }

    func makeModule() -> ProblemsLimitView {
        let problemsLimitComponent = AppGraphBridge.sharedAppGraph.buildProblemsLimitComponent()

        let viewModel = ProblemsLimitViewModel(
            feature: problemsLimitComponent.problemsLimitFeature
        )

        return ProblemsLimitView(viewModel: viewModel, showDivider: showDivider)
    }
}
