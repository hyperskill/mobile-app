import Foundation

protocol TopicToDiscoverNextCardDelegate: AnyObject {
    func doTopicToDiscoverNextCardTapAction(topicID: Int64)
    func doTopicToDiscoverNextCardReloadAction()
}
