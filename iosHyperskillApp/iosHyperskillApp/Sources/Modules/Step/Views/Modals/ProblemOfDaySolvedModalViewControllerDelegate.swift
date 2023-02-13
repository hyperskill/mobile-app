import Foundation

protocol ProblemOfDaySolvedModalViewControllerDelegate: AnyObject {
    func problemOfDaySolvedModalViewControllerDidTapGoToHomescreenButton(
        _ viewController: ProblemOfDaySolvedModalViewController
    )

    func problemOfDaySolvedModalViewControllerDidAppear(_ viewController: ProblemOfDaySolvedModalViewController)

    func problemOfDaySolvedModalViewControllerDidDisappear(_ viewController: ProblemOfDaySolvedModalViewController)
}
