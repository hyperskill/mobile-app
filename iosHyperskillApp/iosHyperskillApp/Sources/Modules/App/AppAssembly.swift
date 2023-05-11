import shared
import UIKit

final class AppAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let feature = AppGraphBridge.sharedAppGraph.mainComponent.appFeature()

        let viewModel = AppViewModel(
            analytic: AppGraphBridge.sharedAppGraph.analyticComponent.analyticInteractor,
            feature: feature
        )

        let viewController = AppViewController(viewModel: viewModel)
        viewModel.viewController = viewController

        return viewController
    }
}
