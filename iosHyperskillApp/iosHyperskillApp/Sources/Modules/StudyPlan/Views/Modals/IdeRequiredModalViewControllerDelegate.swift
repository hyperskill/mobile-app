import Foundation

protocol IdeRequiredModalViewControllerDelegate: AnyObject {
    func ideRequiredModalViewControllerDidTapGoToHomescreenButton(
        _ viewController: IdeRequiredModalViewController
    )

    func ideRequiredModalViewControllerDidAppear(_ viewController: IdeRequiredModalViewController)

    func ideRequiredModalViewControllerDidDisappear(_ viewController: IdeRequiredModalViewController)
}
