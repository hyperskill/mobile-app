import Foundation
import shared

final class TopicCompletedModalViewModel: FeatureViewModel<
  TopicCompletedModalFeature.ViewState,
  TopicCompletedModalFeatureMessage,
  TopicCompletedModalFeatureActionViewAction
> {
    override func shouldNotifyStateDidChange(
        oldState: TopicCompletedModalFeature.ViewState,
        newState: TopicCompletedModalFeature.ViewState
    ) -> Bool {
        false
    }

    func logViewedEvent() {
        // onNewMessage(TopicCompletedModalFeatureMessageViewedEventMessage())
    }
}
