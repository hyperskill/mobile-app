import UIKit

extension UIViewController {
    var isVisible: Bool {
        isViewLoaded && view.window != nil
    }

    @objc
    func present(
        module viewControllerToPresent: UIViewController,
        animated: Bool = true,
        modalPresentationStyle: UIModalPresentationStyle = .fullScreen,
        completion: (() -> Void)? = nil
    ) {
        viewControllerToPresent.modalPresentationStyle = modalPresentationStyle
        present(viewControllerToPresent, animated: animated, completion: completion)
    }

    /// Swaps the old child view controller with a new child view controller within a specified parent view controller.
    /// - Parameters:
    ///   - viewController: The parent view controller where the swap will take place.
    ///   - oldViewController: The current child view controller to be replaced. This parameter is optional.
    ///   - newViewController: The new child view controller to be added.
    ///   - animationDuration: The duration of the transition animation. Default value is 0.33 seconds.
    ///   - animationTransitionOption: The animation transition option for the view controller swap. Default value is `.transitionFlipFromLeft`.
    static func swapRootViewController(
        for viewController: UIViewController,
        from oldViewController: UIViewController?,
        to newViewController: UIViewController,
        animationDuration: TimeInterval = 0.33,
        animationTransitionOption: UIView.AnimationOptions = .transitionFlipFromLeft
    ) {
        oldViewController?.willMove(toParent: nil)

        viewController.addChild(newViewController)

        viewController.view.addSubview(newViewController.view)
        newViewController.view.translatesAutoresizingMaskIntoConstraints = false
        newViewController.view.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }

        newViewController.view.alpha = 0
        newViewController.view.setNeedsLayout()
        newViewController.view.layoutIfNeeded()

        UIView.animate(
            withDuration: animationDuration,
            delay: 0,
            options: animationTransitionOption,
            animations: {
                newViewController.view.alpha = 1
                oldViewController?.view.alpha = 0
            },
            completion: { isFinished in
                guard isFinished else {
                    return
                }

                oldViewController?.view.removeFromSuperview()
                oldViewController?.removeFromParent()

                newViewController.didMove(toParent: viewController)
            }
        )
    }
}
