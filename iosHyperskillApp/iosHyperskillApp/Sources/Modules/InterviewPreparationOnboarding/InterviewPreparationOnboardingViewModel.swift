import Foundation
import shared

final class InterviewPreparationOnboardingViewModel: FeatureViewModel<
  InterviewPreparationOnboardingFeature.State,
  InterviewPreparationOnboardingFeatureMessage,
  InterviewPreparationOnboardingFeatureActionViewAction
> {
    override func shouldNotifyStateDidChange(
        oldState: InterviewPreparationOnboardingFeature.State,
        newState: InterviewPreparationOnboardingFeature.State
    ) -> Bool {
        false
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(InterviewPreparationOnboardingFeatureMessageViewedEventMessage())
    }
}
