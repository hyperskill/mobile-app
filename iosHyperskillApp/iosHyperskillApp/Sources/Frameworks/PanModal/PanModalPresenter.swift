import Foundation
import PanModal

final class PanModalPresenter: ObservableObject {
    private let sourcelessRouter: SourcelessRouter

    init(sourcelessRouter: SourcelessRouter = SourcelessRouter()) {
        self.sourcelessRouter = sourcelessRouter
    }

    func presentPanModal(_ panModal: PanModalPresentableViewController) {
        guard let currentPresentedViewController = sourcelessRouter.currentPresentedViewController() else {
            return
        }

        currentPresentedViewController.presentIfPanModalWithCustomModalPresentationStyle(panModal)
    }

    @discardableResult
    func dismissPanModal(animated: Bool = true, completion: (() -> Void)? = nil) -> Bool {
        guard let currentPresentedViewController = sourcelessRouter.currentPresentedViewController(),
              let panModal = currentPresentedViewController as? UIViewController & PanModalPresentable else {
            return false
        }

        panModal.dismiss(animated: animated, completion: completion)

        return true
    }
}
