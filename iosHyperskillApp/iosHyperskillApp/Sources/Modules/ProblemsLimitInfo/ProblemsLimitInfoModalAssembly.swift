import shared
import UIKit

final class ProblemsLimitInfoModalAssembly: UIKitAssembly {
    private let params: ProblemsLimitInfoModalFeatureParams

    init(params: ProblemsLimitInfoModalFeatureParams) {
        self.params = params
    }

    func makeModule() -> UIViewController {
        let problemsLimitInfoModalComponent =
            AppGraphBridge.sharedAppGraph.buildProblemsLimitInfoModalComponent(params: params)

        let problemsLimitInfoModalViewModel = ProblemsLimitInfoModalViewModel(
            feature: problemsLimitInfoModalComponent.problemsLimitInfoModalFeature
        )

        let problemsLimitInfoModalViewController = ProblemsLimitInfoModalViewController(
            viewModel: problemsLimitInfoModalViewModel
        )

        return problemsLimitInfoModalViewController
    }
}
