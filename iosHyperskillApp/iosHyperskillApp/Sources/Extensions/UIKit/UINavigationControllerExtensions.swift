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
}
