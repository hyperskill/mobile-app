import UIKit

extension UINavigationController {
    /// Remove title for "Back" button on top controller
    func removeBackButtonTitleForTopController() {
        // View controller before last in stack
        guard let parentViewController = viewControllers.dropLast().last else {
            return
        }

        parentViewController.navigationItem.backButtonDisplayMode = .minimal
    }

    /// Pop ViewController with completion handler.
    ///
    /// - Parameters:
    ///   - animated: Set this value to true to animate the transition (default is true).
    ///   - completion: optional completion handler.
    func popViewController(animated: Bool = true, completion: @escaping (() -> Void)) {
        CATransaction.begin()
        CATransaction.setCompletionBlock(completion)
        popViewController(animated: animated)
        CATransaction.commit()
    }

    /// Pop to root viewController with completion handler.
    ///
    /// - Parameters:
    ///   - animated: Set this value to true to animate the transition (default is true).
    ///   - completion: completion handler.
    func popToRootViewController(animated: Bool = true, completion: @escaping (() -> Void)) {
        CATransaction.begin()
        CATransaction.setCompletionBlock(completion)
        popToRootViewController(animated: animated)
        CATransaction.commit()
    }

    /// Push ViewController with completion handler.
    ///
    /// - Parameters:
    ///   - viewController: viewController to push.
    ///   - animated: Set this value to true to animate the transition (default is true).
    ///   - completion: optional completion handler.
    func pushViewController(
        _ viewController: UIViewController,
        animated: Bool = true,
        completion: @escaping (() -> Void)
    ) {
        CATransaction.begin()
        CATransaction.setCompletionBlock(completion)
        pushViewController(viewController, animated: animated)
        CATransaction.commit()
    }
}
