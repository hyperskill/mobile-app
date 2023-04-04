import PanModal
import UIKit

protocol StageImplementUnsupportedModalViewControllerDelegate: AnyObject {
    func stageImplementUnsupportedModalViewControllerViewControllerDidAppear(
        _ viewController: StageImplementUnsupportedModalViewController
    )

    func stageImplementUnsupportedModalViewControllerDidDisappear(
        _ viewController: StageImplementUnsupportedModalViewController
    )

    func stageImplementUnsupportedModalViewControllerDidTapGoToHomescreenButton(
        _ viewController: StageImplementUnsupportedModalViewController
    )
}

final class StageImplementUnsupportedModalViewController: PanModalPresentableViewController {
    weak var delegate: StageImplementUnsupportedModalViewControllerDelegate?

    override var shortFormHeight: PanModalHeight { .contentHeight(view.intrinsicContentSize.height) }

    override var longFormHeight: PanModalHeight { shortFormHeight }

    init(delegate: StageImplementUnsupportedModalViewControllerDelegate?) {
        self.delegate = delegate
        super.init()
    }

    override func loadView() {
        let view = StageImplementUnsupportedModalView()
        view.onCallToActionButtonTapped = { [weak self] in
            guard let strongSelf = self else {
                return
            }

            strongSelf.delegate?.stageImplementUnsupportedModalViewControllerDidTapGoToHomescreenButton(strongSelf)
        }

        self.view = view
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)

        DispatchQueue.main.async {
            self.panModalSetNeedsLayoutUpdate()
            self.panModalTransition(to: .shortForm)
        }
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        delegate?.stageImplementUnsupportedModalViewControllerViewControllerDidAppear(self)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        delegate?.stageImplementUnsupportedModalViewControllerDidDisappear(self)
    }
}
