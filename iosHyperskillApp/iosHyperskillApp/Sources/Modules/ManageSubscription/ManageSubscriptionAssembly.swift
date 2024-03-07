import shared
import SwiftUI

final class ManageSubscriptionAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let manageSubscriptionComponent = AppGraphBridge.sharedAppGraph.buildManageSubscriptionComponent()

        let manageSubscriptionViewModel = ManageSubscriptionViewModel(
            feature: manageSubscriptionComponent.manageSubscriptionFeature
        )

        let manageSubscriptionView = ManageSubscriptionView(
            viewModel: manageSubscriptionViewModel
        )

        let hostingController = StyledHostingController(
            rootView: manageSubscriptionView
        )
        hostingController.navigationItem.largeTitleDisplayMode = .never

        return hostingController
    }
}
