import PanModal
import shared
import UIKit

final class ProblemsLimitInfoModalViewController: PanModalPresentableViewController {
    private let viewModel: ProblemsLimitInfoModalViewModel

    var problemsLimitInfoModalView: ProblemsLimitInfoModalView? { view as? ProblemsLimitInfoModalView }

    override var shortFormHeight: PanModalHeight { .contentHeight(view.intrinsicContentSize.height) }

    override var longFormHeight: PanModalHeight { shortFormHeight }

    init(viewModel: ProblemsLimitInfoModalViewModel) {
        self.viewModel = viewModel
        super.init()
    }

    override func loadView() {
        let view = ProblemsLimitInfoModalView()
        self.view = view
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        setup()
        problemsLimitInfoModalView?.renderState(viewModel.state)
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)

        viewModel.startListening()

        DispatchQueue.main.async {
            self.panModalSetNeedsLayoutUpdate()
            self.panModalTransition(to: .shortForm)
        }
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

    private func setup() {
        problemsLimitInfoModalView?.onUnlockLimitsButtonTap = { [weak self] in
            FeedbackGenerator(feedbackType: .selection).triggerFeedback()
            self?.viewModel.doUnlockUnlimitedProblems()
        }
        problemsLimitInfoModalView?.onGoToHomescreenButtonTap = { [weak self] in
            FeedbackGenerator(feedbackType: .selection).triggerFeedback()
            self?.viewModel.doGoToHomeScreen()
        }

        viewModel.onViewAction = handleViewAction(_:)
    }

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
        case .home:
            dismiss(animated: true) {
                SourcelessRouter().currentNavigation?.popToRootViewController(animated: true) {
                    TabBarRouter(tab: .home).route()
                }
            }
        case .paywall(let navigateToPaywallViewAction):
            dismiss(animated: true) {
                let assembly = PaywallAssembly(source: navigateToPaywallViewAction.paywallTransitionSource)
                SourcelessRouter().currentPresentedViewController()?.present(module: assembly.makeModule())
            }
        }
    }
}
