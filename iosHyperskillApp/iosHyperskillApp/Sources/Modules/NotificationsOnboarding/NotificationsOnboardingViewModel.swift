import Foundation
import shared

final class NotificationsOnboardingViewModel: FeatureViewModel<
  NotificationsOnboardingFeature.State,
  NotificationsOnboardingFeatureMessage,
  NotificationsOnboardingFeatureActionViewAction
> {
    override func shouldNotifyStateDidChange(
        oldState: NotificationsOnboardingFeature.State,
        newState: NotificationsOnboardingFeature.State
    ) -> Bool {
        false
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(NotificationsOnboardingFeatureMessageViewedEventMessage())
    }
}
