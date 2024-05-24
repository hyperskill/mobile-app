import Foundation
import shared

final class TopicCompletedModalViewModel: FeatureViewModel<
  TopicCompletedModalFeature.ViewState,
  TopicCompletedModalFeatureMessage,
  TopicCompletedModalFeatureActionViewAction
> {
    weak var moduleOutput: TopicCompletedModalOutputProtocol?

    override func shouldNotifyStateDidChange(
        oldState: TopicCompletedModalFeature.ViewState,
        newState: TopicCompletedModalFeature.ViewState
    ) -> Bool {
        false
    }

    func doCallToAction() {
        onNewMessage(TopicCompletedModalFeatureMessageCallToActionButtonClicked())
    }

    func doCloseAction() {
        onNewMessage(TopicCompletedModalFeatureMessageCloseButtonClicked())
    }

    func logShownEvent() {
        onNewMessage(TopicCompletedModalFeatureMessageShownEventMessage())
    }

    func logHiddenEvent() {
        onNewMessage(TopicCompletedModalFeatureMessageHiddenEventMessage())
    }

    func doNextTopicPresentation() {
        moduleOutput?.topicCompletedModalDidRequestContinueWithNextTopic()
    }

    func doStudyPlanPresentation() {
        moduleOutput?.topicCompletedModalDidRequestGoToStudyPlan()
    }
}
