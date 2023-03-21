import Foundation

protocol ProblemsLimitReachedModalViewControllerDelegate: AnyObject {
    func problemsLimitReachedModalViewControllerDidTapGoToHomescreenButton(
        _ viewController: ProblemsLimitReachedModalViewController
    )

    func problemsLimitReachedModalViewControllerDidAppear(_ viewController: ProblemsLimitReachedModalViewController)

    func problemsLimitReachedModalViewControllerDidDisappear(_ viewController: ProblemsLimitReachedModalViewController)
}
