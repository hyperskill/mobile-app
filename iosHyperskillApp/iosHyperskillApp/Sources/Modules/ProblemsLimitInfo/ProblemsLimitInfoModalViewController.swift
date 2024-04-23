import PanModal
import shared
import UIKit

final class ProblemsLimitInfoModalViewController: PanModalSwiftUIViewController<ProblemsLimitInfoModalView> {
    private let viewModel: ProblemsLimitInfoModalViewModel

    override var shouldUpdateAdditionalSafeAreaInsets: Bool { false }

    init(viewModel: ProblemsLimitInfoModalViewModel) {
        self.viewModel = viewModel

        let view = ProblemsLimitInfoModalView(
            viewState: viewModel.state,
            onCallToActionButtonTap: viewModel.doUnlockUnlimitedProblems
        )

        super.init(
            isPresented: .constant(false),
            content: { view }
        )

        self.viewModel.onViewAction = handleViewAction(_:)
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        viewModel.startListening()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        viewModel.logShownEvent()
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        viewModel.stopListening()
    }

    override func panModalWillDismiss() {
        viewModel.logHiddenEvent()
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: ProblemsLimitInfoModalFeatureActionViewAction) {
        switch ProblemsLimitInfoModalFeatureActionViewActionKs(viewAction) {
        case .navigateTo(let navigateToViewAction):
            handleNavigateToViewAction(navigateToViewAction)
        }
    }

    private func handleNavigateToViewAction(
        _ viewAction: ProblemsLimitInfoModalFeatureActionViewActionNavigateTo
    ) {
        switch ProblemsLimitInfoModalFeatureActionViewActionNavigateToKs(viewAction) {
        case .paywall(let navigateToPaywallViewAction):
            dismiss(animated: true) {
                let assembly = PaywallAssembly(source: navigateToPaywallViewAction.paywallTransitionSource)
                SourcelessRouter().currentPresentedViewController()?.present(module: assembly.makeModule())
            }
        }
    }
}
