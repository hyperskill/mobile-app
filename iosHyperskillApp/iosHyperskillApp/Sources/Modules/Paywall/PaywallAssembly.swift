import shared
import SwiftUI

struct PaywallPresentationContext {
    let source: PaywallTransitionSource
    let moduleOutput: PaywallOutputProtocol?
}

final class PaywallAssembly: UIKitAssembly {
    private let source: PaywallTransitionSource

    private weak var moduleOutput: PaywallOutputProtocol?

    init(context: PaywallPresentationContext) {
        self.source = context.source
        self.moduleOutput = context.moduleOutput
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
        let hostingController = PaywallHostingController(rootView: paywallView)
        hostingController.navigationItem.largeTitleDisplayMode = .never
        hostingController.title = Strings.Paywall.navigationTitle
        hostingController.modalPresentationStyle = .fullScreen

        return hostingController
    }
}
