import shared
import SwiftUI

final class ManageSubscriptionAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let manageSubscriptionComponent = AppGraphBridge.sharedAppGraph.buildManageSubscriptionComponent()

        let manageSubscriptionViewModel = ManageSubscriptionViewModel(
            feature: manageSubscriptionComponent.manageSubscriptionFeature
        )

        let stackRouter = StackRouter()

        let manageSubscriptionView = ManageSubscriptionView(
            viewModel: manageSubscriptionViewModel,
            stackRouter: stackRouter
        )

        let hostingController = StyledHostingController(
            rootView: manageSubscriptionView
        )
        hostingController.navigationItem.largeTitleDisplayMode = .never
        hostingController.title = Strings.ManageSubscription.navigationTitle

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
