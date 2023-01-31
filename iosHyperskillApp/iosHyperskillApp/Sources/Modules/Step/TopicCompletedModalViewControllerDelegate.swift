import Foundation

protocol TopicCompletedModalViewControllerDelegate: AnyObject {
    func topicCompletedModalViewControllerDidTapGoToHomescreenButton(
        _ viewController: TopicCompletedModalViewController
    )

    func logTopicCompletedModalShownEvent()

    func logTopicCompletedModalHiddenEvent()
}
