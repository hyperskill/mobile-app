import shared
import SwiftUI

final class PaywallAssembly: UIKitAssembly {
    private let source: PaywallTransitionSource

    private weak var moduleOutput: PaywallOutputProtocol?

    init(source: PaywallTransitionSource, moduleOutput: PaywallOutputProtocol?) {
        self.source = source
        self.moduleOutput = moduleOutput
    }

    func makeModule() -> UIViewController {
        let paywallComponent = AppGraphBridge.sharedAppGraph.buildPaywallComponent(
            paywallTransitionSource: source
        )

        let paywallViewModel = PaywallViewModel(
            feature: paywallComponent.paywallFeature
        )
        paywallViewModel.moduleOutput = moduleOutput

        let paywallView = PaywallView(
            viewModel: paywallViewModel
        )
        let hostingController = StyledHostingController(
            rootView: paywallView
        )
        hostingController.navigationItem.largeTitleDisplayMode = .never
        hostingController.title = Strings.Paywall.navigationTitle

        let shouldEmbedIntoNavigationController = PaywallToolbarVisibilityResolver.shared.isToolbarVisible(
            paywallTransitionSource: source
        )

        return shouldEmbedIntoNavigationController
            ? UINavigationController(rootViewController: hostingController)
            : hostingController
    }
}
