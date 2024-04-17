import shared
import UIKit

final class ProblemsLimitReachedModalAssembly: UIKitAssembly {
    private let params: ProblemsLimitReachedModalFeatureParams

    init(params: ProblemsLimitReachedModalFeatureParams) {
        self.params = params
    }

    func makeModule() -> UIViewController {
        let problemsLimitReachedModalComponent =
            AppGraphBridge.sharedAppGraph.buildProblemsLimitReachedModalComponent(params: params)

        let problemsLimitReachedModalViewModel = ProblemsLimitReachedModalViewModel(
            feature: problemsLimitReachedModalComponent.problemsLimitReachedModalFeature
        )

        let problemsLimitReachedModalViewController = ProblemsLimitReachedModalViewController(
            viewModel: problemsLimitReachedModalViewModel
        )

        return problemsLimitReachedModalViewController
    }
}
