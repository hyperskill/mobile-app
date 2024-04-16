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
        performPopNavigationAction(
            action: { self.popViewController(animated: animated) },
            animated: animated,
            completion: completion
        )
    }

    /// Pop to root viewController with completion handler.
    ///
    /// - Parameters:
    ///   - animated: Set this value to true to animate the transition (default is true).
    ///   - completion: completion handler.
    func popToRootViewController(animated: Bool = true, completion: @escaping (() -> Void)) {
        performPopNavigationAction(
            action: { self.popToRootViewController(animated: animated) },
            animated: animated,
            completion: completion
        )
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
        performNavigationAction(
            action: { self.pushViewController(viewController, animated: animated) },
            animated: animated,
            completion: completion
        )
    }

    private func performPopNavigationAction(action: () -> Void, animated: Bool, completion: @escaping () -> Void) {
        guard viewControllers.count > 1 else {
            return completion()
        }

        performNavigationAction(action: action, animated: animated, completion: completion)
    }

    private func performNavigationAction(action: () -> Void, animated: Bool, completion: @escaping () -> Void) {
        if animated {
            let startTime = Date()
            let minimumAnimationDuration: TimeInterval = 0.35
            // The completion is guaranteed to be called not before 0.35 seconds,
            // aligning with the minimum desired animation duration.
            CATransaction.begin()
            CATransaction.setCompletionBlock {
                let elapsedTime = Date().timeIntervalSince(startTime)
                let delay = max(0, minimumAnimationDuration - elapsedTime)
                DispatchQueue.main.asyncAfter(deadline: .now() + delay) {
                    completion()
                }
            }
            action()
            CATransaction.commit()
        } else {
            action()
            completion()
        }
    }
}
