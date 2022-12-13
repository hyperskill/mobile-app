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

    func dismissPanModal(animated: Bool) {
        guard let currentPresentedViewController = sourcelessRouter.currentPresentedViewController(),
              let panModal = currentPresentedViewController as? UIViewController & PanModalPresentable else {
            return
        }

        panModal.dismiss(animated: animated)
    }
}
