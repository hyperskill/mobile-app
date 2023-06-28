import shared
import UIKit

final class TopicsRepetitionsViewModel: FeatureViewModel<
  TopicsRepetitionsFeatureState,
  TopicsRepetitionsFeatureMessage,
  TopicsRepetitionsFeatureActionViewAction
> {
    var stateKs: TopicsRepetitionsFeatureStateKs { .init(state) }

    init(feature: Presentation_reduxFeature) {
        super.init(feature: feature)
        onNewMessage(TopicsRepetitionsFeatureMessageInitialize(forceUpdate: false))
    }

    override func shouldNotifyStateDidChange(
        oldState: TopicsRepetitionsFeatureState,
        newState: TopicsRepetitionsFeatureState
    ) -> Bool {
        TopicsRepetitionsFeatureStateKs(oldState) != TopicsRepetitionsFeatureStateKs(newState)
    }

    func doRetryLoadTopicsRepetitions() {
        onNewMessage(TopicsRepetitionsFeatureMessageInitialize(forceUpdate: true))
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
