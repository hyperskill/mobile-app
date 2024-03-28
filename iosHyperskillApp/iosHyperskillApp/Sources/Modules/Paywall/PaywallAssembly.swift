import shared
import SwiftUI

struct PaywallPresentationContext {
    let source: PaywallTransitionSource
}

final class PaywallAssembly: UIKitAssembly {
    private let source: PaywallTransitionSource

    init(context: PaywallPresentationContext) {
        self.source = context.source
    }

    func makeModule() -> UIViewController {
        let paywallComponent = AppGraphBridge.sharedAppGraph.buildPaywallComponent(
            paywallTransitionSource: source
        )

        let paywallViewModel = PaywallViewModel(
            feature: paywallComponent.paywallFeature
        )

        let paywallView = PaywallView(
            viewModel: paywallViewModel
        )
        let hostingController = PaywallHostingController(rootView: paywallView)
        hostingController.navigationItem.largeTitleDisplayMode = .never
        hostingController.title = Strings.Paywall.navigationTitle
        hostingController.modalPresentationStyle = .fullScreen

        return hostingController
    }
}
