import Foundation
import shared

final class ProblemsLimitAssembly: Assembly {
    private let appearance: ProblemsLimitView.Appearance

    init(appearance: ProblemsLimitView.Appearance = ProblemsLimitView.Appearance()) {
        self.appearance = appearance
    }

    func makeModule() -> ProblemsLimitView {
        let problemsLimitComponent = AppGraphBridge.sharedAppGraph.buildProblemsLimitComponent()

        let viewModel = ProblemsLimitViewModel(
            feature: problemsLimitComponent.problemsLimitFeature
        )

        return ProblemsLimitView(appearance: appearance, viewModel: viewModel)
    }
}
