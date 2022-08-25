import Foundation
import shared

final class OnboardingViewModel: FeatureViewModel<
  OnboardingFeatureState,
  OnboardingFeatureMessage,
  OnboardingFeatureActionViewAction
> {
    weak var moduleOutput: OnboardingOutputProtocol?

    func loadOnboarding() {
        onNewMessage(OnboardingFeatureMessageInit())
    }

    func doSignPresentation() {
        moduleOutput?.handleOnboardingSignInRequested()
    }

    func doSignUpPresentation() {
        moduleOutput?.handleOnboardingSignUpRequested()
    }

    func logViewedEvent() {
        onNewMessage(OnboardingFeatureMessageOnboardingViewedEventMessage())
    }
}
