import SwiftUI
import UIKit

protocol ModalRouterProtocol: AnyObject {
    func present(
        module viewControllerToPresent: UIViewController,
        animated: Bool,
        modalPresentationStyle: UIModalPresentationStyle,
        completion: (() -> Void)?
    )

    func presentAlert(_ alertController: UIAlertController, animated: Bool, completion: (() -> Void)?)
}

extension ModalRouterProtocol {
    func present(
        module viewControllerToPresent: UIViewController,
        animated: Bool = true,
        modalPresentationStyle: UIModalPresentationStyle = .fullScreen,
        completion: (() -> Void)? = nil
    ) {
        present(
            module: viewControllerToPresent,
            animated: animated,
            modalPresentationStyle: modalPresentationStyle,
            completion: completion
        )
    }

    func presentAlert(_ alertController: UIAlertController, animated: Bool = true, completion: (() -> Void)? = nil) {
        presentAlert(alertController, animated: animated, completion: completion)
    }
}

class ModalRouter: ModalRouterProtocol {
    weak var rootViewController: UIViewController?

    init(rootViewController: UIViewController? = nil) {
        self.rootViewController = rootViewController
    }

    func present(
        module viewControllerToPresent: UIViewController,
        animated: Bool,
        modalPresentationStyle: UIModalPresentationStyle,
        completion: (() -> Void)?
    ) {
        guard let presentingViewController = getViewControllerToPresentFrom() else {
            return assertionFailure("ModalRouter :: presentingViewController is nil")
        }

        presentingViewController.present(
            module: viewControllerToPresent,
            animated: animated,
            modalPresentationStyle: modalPresentationStyle,
            completion: nil
        )
    }

    func presentAlert(_ alertController: UIAlertController, animated: Bool, completion: (() -> Void)?) {
        guard let presentingViewController = getViewControllerToPresentFrom() else {
            return assertionFailure("ModalRouter :: presentingViewController is nil")
        }

        presentingViewController.present(alertController, animated: animated, completion: completion)
    }

    private func getViewControllerToPresentFrom() -> UIViewController? {
        guard let rootViewController else {
            return nil
        }

        var result = rootViewController

        while let presentedViewController = result.presentedViewController {
            result = presentedViewController
        }

        return result
    }
}

class SwiftUIModalRouter: ModalRouter, ObservableObject {}
