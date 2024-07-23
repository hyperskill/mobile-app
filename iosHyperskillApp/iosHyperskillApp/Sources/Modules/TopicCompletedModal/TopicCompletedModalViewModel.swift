import Foundation
import shared

final class TopicCompletedModalViewModel: FeatureViewModel<
  TopicCompletedModalFeature.ViewState,
  TopicCompletedModalFeatureMessage,
  TopicCompletedModalFeatureActionViewAction
> {
    weak var moduleOutput: TopicCompletedModalOutputProtocol?

    init(feature: Presentation_reduxFeature) {
        super.init(feature: feature)
        addNotificationObservers()
    }

    deinit {
        removeNotificationObservers()
    }

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

    func doPaywallPresentation(paywallTransitionSource: PaywallTransitionSource) {
        moduleOutput?.topicCompletedModalDidRequestPaywall(paywallTransitionSource: paywallTransitionSource)
    }
}

private extension TopicCompletedModalViewModel {
    func addNotificationObservers() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleUserDidTakeScreenshot),
            name: UIApplication.userDidTakeScreenshotNotification,
            object: nil
        )
    }

    func removeNotificationObservers() {
        NotificationCenter.default.removeObserver(
            self,
            name: UIApplication.userDidTakeScreenshotNotification,
            object: nil
        )
    }

    @objc
    func handleUserDidTakeScreenshot() {
        onNewMessage(TopicCompletedModalFeatureMessageUserDidTakeScreenshotEventMessage())
    }
}
