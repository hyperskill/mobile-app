import shared
import UIKit

final class TopicsRepetitionsViewModel: FeatureViewModel<
  TopicsRepetitionsFeatureState,
  TopicsRepetitionsFeatureMessage,
  TopicsRepetitionsFeatureActionViewAction
> {
    var stateKs: TopicsRepetitionsFeatureStateKs { .init(state) }

    override func shouldNotifyStateDidChange(
        oldState: TopicsRepetitionsFeatureState,
        newState: TopicsRepetitionsFeatureState
    ) -> Bool {
        TopicsRepetitionsFeatureStateKs(oldState) != TopicsRepetitionsFeatureStateKs(newState)
    }

    func doLoadContent(forceUpdate: Bool = false) {
        onNewMessage(TopicsRepetitionsFeatureMessageInitialize(forceUpdate: forceUpdate))
    }

    func doLoadNextTopics() {
        onNewMessage(TopicsRepetitionsFeatureMessageShowMoreButtonClicked())
    }

    func doRepeatTopic(topicID: Int64) {
        onNewMessage(TopicsRepetitionsFeatureMessageRepeatTopicClicked(topicId: topicID))
    }

    func doRepeatNextTopic() {
        onNewMessage(TopicsRepetitionsFeatureMessageRepeatNextTopicClicked())
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(TopicsRepetitionsFeatureMessageViewedEventMessage())
    }
}
