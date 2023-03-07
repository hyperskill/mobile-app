import Foundation

protocol StageImplementUnsupportedModalViewControllerDelegate: AnyObject {
    func stageImplementUnsupportedModalViewControllerViewControllerDidAppear(
        _ viewController: StageImplementUnsupportedModalViewController
    )

    func stageImplementUnsupportedModalViewControllerDidDisappear(
        _ viewController: StageImplementUnsupportedModalViewController
    )

    func stageImplementUnsupportedModalViewControllerDidTapGoToHomescreenButton(
        _ viewController: StageImplementUnsupportedModalViewController
    )
}
