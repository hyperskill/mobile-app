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

    func doTopicStepQuizPresentation(stepID: Int64) {
        mainScheduler.schedule {
            self.onViewAction?(TopicsRepetitionsFeatureActionViewActionNavigateToStepScreen(stepId: stepID))
        }
    }

    // MARK: Analytic

    func logClickedRepeatNextTopicEvent() {
        onNewMessage(TopicsRepetitionsFeatureMessageClickedRepeatNextTopicEventMessage())
    }

    func logClickedRepeatTopicEvent() {
        onNewMessage(TopicsRepetitionsFeatureMessageClickedRepeatTopicEventMessage())
    }
}
