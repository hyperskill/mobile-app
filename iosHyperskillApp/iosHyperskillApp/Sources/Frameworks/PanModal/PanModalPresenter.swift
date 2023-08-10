import Foundation
import PanModal

final class PanModalPresenter: ObservableObject {
    private let sourcelessRouter: SourcelessRouter

    weak var rootViewController: UIViewController?

    init(sourcelessRouter: SourcelessRouter = SourcelessRouter(), rootViewController: UIViewController? = nil) {
        self.sourcelessRouter = sourcelessRouter
        self.rootViewController = rootViewController
    }

    func presentPanModal(_ panModal: UIViewController & PanModalPresentable) {
        let presentationViewController = rootViewController ?? sourcelessRouter.currentPresentedViewController()

        guard let presentationViewController else {
            return assertionFailure("PanModalPresenter :: presentationViewController is nil")
        }

        presentationViewController.presentIfPanModalWithCustomModalPresentationStyle(panModal)
    }

    @discardableResult
    func presentIfPanModal(_ viewControllerToPresent: UIViewController) -> Bool {
        if let panModalPresentableViewController = viewControllerToPresent as? UIViewController & PanModalPresentable {
            presentPanModal(panModalPresentableViewController)
            return true
        } else {
            return false
        }
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
