import shared
import UIKit

final class ProblemsLimitInfoModalAssembly: UIKitAssembly {
    private let params: ProblemsLimitInfoModalFeatureParams

    init(params: ProblemsLimitInfoModalFeatureParams) {
        self.params = params
    }

    func makeModule() -> UIViewController {
        let problemsLimitReachedModalComponent =
            AppGraphBridge.sharedAppGraph.buildProblemsLimitInfoModalComponent(params: params)

        let problemsLimitReachedModalViewModel = ProblemsLimitInfoModalViewModel(
            feature: problemsLimitReachedModalComponent.problemsLimitInfoModalFeature
        )

        let problemsLimitReachedModalViewController = ProblemsLimitInfoModalViewController(
            viewModel: problemsLimitReachedModalViewModel
        )

        return problemsLimitReachedModalViewController
    }
}
