import shared
import UIKit

final class TopicsRepetitionsViewModel: FeatureViewModel<
  TopicsRepetitionsFeatureState,
  TopicsRepetitionsFeatureMessage,
  TopicsRepetitionsFeatureActionViewAction
> {
    var stateKs: TopicsRepetitionsFeatureStateKs { .init(state) }

    func doLoadContent(forceUpdate: Bool = false) {
        onNewMessage(TopicsRepetitionsFeatureMessageInitialize(forceUpdate: forceUpdate))
    }

    func doLoadNextTopics() {
        onNewMessage(TopicsRepetitionsFeatureMessageShowMoreButtonClicked())
    }

    func doTopicStepQuizPresentation(stepID: Int64) {
        DispatchQueue.main.async {
            self.onViewAction?(
                TopicsRepetitionsFeatureActionViewActionNavigateToStepScreen(stepId: stepID)
            )
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
