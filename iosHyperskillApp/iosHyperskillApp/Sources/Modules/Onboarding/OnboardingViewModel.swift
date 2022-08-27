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
        logClickedSignInEvent()
        moduleOutput?.handleOnboardingSignInRequested()
    }

    func doSignUpPresentation() {
        logClickedSignUnEvent()
        moduleOutput?.handleOnboardingSignUpRequested()
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(OnboardingFeatureMessageOnboardingViewedEventMessage())
    }

    private func logClickedSignInEvent() {
        onNewMessage(OnboardingFeatureMessageOnboardingClickedSignInEventMessage())
    }

    private func logClickedSignUnEvent() {
        onNewMessage(OnboardingFeatureMessageOnboardingClickedSignUnEventMessage())
    }
}
