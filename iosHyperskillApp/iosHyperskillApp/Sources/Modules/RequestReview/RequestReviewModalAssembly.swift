import shared
import UIKit

final class RequestReviewModalAssembly: UIKitAssembly {
    private let stepRoute: StepRoute

    init(stepRoute: StepRoute) {
        self.stepRoute = stepRoute
    }

    func makeModule() -> UIViewController {
        let requestReviewModalComponent = AppGraphBridge.sharedAppGraph.buildRequestReviewModalComponent(
            stepRoute: stepRoute
        )

        let requestReviewModalViewModel = RequestReviewModalViewModel(
            feature: requestReviewModalComponent.requestReviewModalFeature
        )

        let requestReviewModalViewController = RequestReviewModalViewController(
            viewModel: requestReviewModalViewModel
        )
        requestReviewModalViewModel.viewController = requestReviewModalViewController

        return requestReviewModalViewController
    }
}
