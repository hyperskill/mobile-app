import Foundation

protocol TopicCompletedModalViewControllerDelegate: AnyObject {
    func topicCompletedModalViewControllerDidTapGoToHomescreenButton(
        _ viewController: TopicCompletedModalViewController
    )

    func topicCompletedModalViewControllerDidAppear(_ viewController: TopicCompletedModalViewController)

    func topicCompletedModalViewControllerDidDisappear(_ viewController: TopicCompletedModalViewController)
}
