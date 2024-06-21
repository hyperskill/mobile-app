import Foundation
import shared

final class WelcomeOnboardingTrackDetailsViewModel: FeatureViewModel<
  WelcomeOnboardingTrackDetailsFeature.ViewState,
  WelcomeOnboardingTrackDetailsFeatureMessage,
  WelcomeOnboardingTrackDetailsFeatureActionViewAction
> {
    weak var moduleOutput: WelcomeOnboardingTrackDetailsOutputProtocol?

    override func shouldNotifyStateDidChange(
        oldState: WelcomeOnboardingTrackDetailsFeature.ViewState,
        newState: WelcomeOnboardingTrackDetailsFeature.ViewState
    ) -> Bool {
        !oldState.isEqual(newState)
    }

    func doCallToAction() {
        onNewMessage(WelcomeOnboardingTrackDetailsFeatureMessageContinueClicked())
    }

    func doNotifyTrackSelected() {
        moduleOutput?.handleWelcomeOnboardingTrackDetailsTrackSelected(track: state.track)
    }

    func logViewedEvent() {
        onNewMessage(WelcomeOnboardingTrackDetailsFeatureMessageViewedEventMessage())
    }
}
