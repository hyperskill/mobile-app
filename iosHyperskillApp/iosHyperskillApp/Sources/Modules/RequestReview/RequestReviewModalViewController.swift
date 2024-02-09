import PanModal
import shared
import StoreKit
import UIKit

protocol RequestReviewModalViewControllerProtocol: AnyObject {
    func displayState(_ state: RequestReviewModalFeature.ViewState)
    func displayViewAction(_ viewAction: RequestReviewModalFeatureActionViewActionKs)
}

final class RequestReviewModalViewController: PanModalPresentableViewController {
    private let viewModel: RequestReviewModalViewModel

    var requestReviewModalView: RequestReviewModalView? { view as? RequestReviewModalView }

    override var shortFormHeight: PanModalHeight { .contentHeight(view.intrinsicContentSize.height) }

    override var longFormHeight: PanModalHeight { shortFormHeight }

    init(viewModel: RequestReviewModalViewModel) {
        self.viewModel = viewModel
        super.init()
    }

    override func loadView() {
        let view = RequestReviewModalView()
        view.onPositiveButtonTap = { [weak self] in
            FeedbackGenerator(feedbackType: .selection).triggerFeedback()
            self?.viewModel.doPositiveButtonAction()
        }
        view.onNegativeButtonTap = { [weak self] in
            FeedbackGenerator(feedbackType: .selection).triggerFeedback()
            self?.viewModel.doNegativeButtonAction()
        }
        self.view = view
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        viewModel.logShownEvent()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        viewModel.startListening()
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        viewModel.stopListening()
    }

    override func panModalWillDismiss() {
        viewModel.logHiddenEvent()
    }
}

extension RequestReviewModalViewController: RequestReviewModalViewControllerProtocol {
    func displayState(_ state: RequestReviewModalFeature.ViewState) {
        requestReviewModalView?.renderState(state)

        panModalSetNeedsLayoutUpdate()
        panModalTransition(to: .shortForm)
    }

    func displayViewAction(_ viewAction: RequestReviewModalFeatureActionViewActionKs) {
        switch viewAction {
        case .dismiss:
            dismiss(animated: true)
        case .requestUserReview:
            dismiss(
                animated: true,
                completion: {
                    if let scene = UIApplication.shared.connectedScenes.first(
                        where: { $0.activationState == .foregroundActive }
                    ) as? UIWindowScene {
                        SKStoreReviewController.requestReview(in: scene)
                    }
                }
            )
        case .submitSupportRequest(let submitSupportRequestViewAction):
            dismiss(
                animated: true,
                completion: {
                    WebControllerManager.shared.presentWebControllerWithURLString(
                        submitSupportRequestViewAction.url,
                        controllerType: .safari
                    )
                }
            )
        }
    }
}
