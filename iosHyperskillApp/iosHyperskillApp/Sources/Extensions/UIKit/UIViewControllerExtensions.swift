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
}
