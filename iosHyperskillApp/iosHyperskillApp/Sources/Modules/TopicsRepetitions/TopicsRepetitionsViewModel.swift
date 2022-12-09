import shared
import UIKit

final class TopicsRepetitionsViewModel: FeatureViewModel<
  TopicsRepetitionsFeatureState,
  TopicsRepetitionsFeatureMessage,
  TopicsRepetitionsFeatureActionViewAction
> {
    private let recommendedRepetitionsCount: Int32

    init(
        recommendedRepetitionsCount: Int32,
        feature: Presentation_reduxFeature
    ) {
        self.recommendedRepetitionsCount = recommendedRepetitionsCount
        super.init(feature: feature)
    }

    var stateKs: TopicsRepetitionsFeatureStateKs { .init(state) }

    override func shouldNotifyStateDidChange(
        oldState: TopicsRepetitionsFeatureState,
        newState: TopicsRepetitionsFeatureState
    ) -> Bool {
        TopicsRepetitionsFeatureStateKs(oldState) != TopicsRepetitionsFeatureStateKs(newState)
    }

    func doLoadContent(forceUpdate: Bool = false) {
        onNewMessage(
            TopicsRepetitionsFeatureMessageInitialize(
                recommendedRepetitionsCount: recommendedRepetitionsCount,
                forceUpdate: forceUpdate
            )
        )
    }

    func doLoadNextTopics() {
        onNewMessage(TopicsRepetitionsFeatureMessageShowMoreButtonClicked())
    }

    func doRepeatTopic(stepID: Int64) {
        onNewMessage(TopicsRepetitionsFeatureMessageRepeatTopicClicked(stepId: stepID))
    }

    func doRepeatNextTopic() {
        onNewMessage(TopicsRepetitionsFeatureMessageRepeatNextTopicClicked())
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(TopicsRepetitionsFeatureMessageViewedEventMessage())
    }
}
